resource "aws_kinesis_stream" "this" {
  for_each = toset(var.stream_names)

  name             = each.value
  shard_count      = var.shard_count
  retention_period = var.retention_period

  encryption_type = var.encryption_enabled ? "KMS" : "NONE"
  kms_key_id      = var.encryption_enabled ? var.kms_key_id != null ? var.kms_key_id : null : null

  stream_mode_details {
    stream_mode = upper(var.stream_mode)
  }

  tags = merge(var.tags, {
    Name = each.value
  })
}
