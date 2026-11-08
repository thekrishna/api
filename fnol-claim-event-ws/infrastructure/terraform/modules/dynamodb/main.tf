locals {
  table_map = { for table in var.tables : table.name => table }
}

resource "aws_dynamodb_table" "this" {
  for_each = local.table_map

  name         = each.value.name
  billing_mode = each.value.billing_mode != null ? each.value.billing_mode : "PAY_PER_REQUEST"
  hash_key     = each.value.hash_key
  range_key    = each.value.range_key

  read_capacity  = each.value.billing_mode == "PROVISIONED" ? coalesce(each.value.read_capacity, 5) : null
  write_capacity = each.value.billing_mode == "PROVISIONED" ? coalesce(each.value.write_capacity, 5) : null

  dynamic "attribute" {
    for_each = distinct(concat(
      [{ name = each.value.hash_key, type = each.value.hash_key_type }],
      each.value.range_key != null ? [{ name = each.value.range_key, type = each.value.range_key_type != null ? each.value.range_key_type : "S" }] : [],
      flatten([
        for gsi in each.value.global_secondary_indexes : [
          { name = gsi.hash_key, type = gsi.hash_key_type },
          gsi.range_key != null ? { name = gsi.range_key, type = coalesce(gsi.range_key_type, "S") } : null,
        ]
      ])
    ))
    content {
      name = attribute.value.name
      type = attribute.value.type
    }
  }

  dynamic "global_secondary_index" {
    for_each = each.value.global_secondary_indexes
    content {
      name            = global_secondary_index.value.name
      hash_key        = global_secondary_index.value.hash_key
      range_key       = global_secondary_index.value.range_key
      projection_type = coalesce(global_secondary_index.value.projection_type, "ALL")
      read_capacity   = each.value.billing_mode == "PROVISIONED" ? coalesce(global_secondary_index.value.read_capacity, 5) : null
      write_capacity  = each.value.billing_mode == "PROVISIONED" ? coalesce(global_secondary_index.value.write_capacity, 5) : null
    }
  }

  server_side_encryption {
    enabled     = true
    kms_key_arn = var.kms_key_arn
  }

  point_in_time_recovery {
    enabled = coalesce(each.value.point_in_time_recovery, true)
  }

  deletion_protection_enabled = coalesce(each.value.deletion_protection, false)

  dynamic "ttl" {
    for_each = each.value.ttl_enabled == true ? [1] : []
    content {
      enabled        = true
      attribute_name = coalesce(each.value.ttl_attribute_name, "expiresAt")
    }
  }

  stream_enabled   = coalesce(each.value.stream_enabled, false)
  stream_view_type = each.value.stream_enabled == true ? coalesce(each.value.stream_view_type, "NEW_AND_OLD_IMAGES") : null

  tags = merge(var.tags, {
    Name = each.value.name
  })
}
