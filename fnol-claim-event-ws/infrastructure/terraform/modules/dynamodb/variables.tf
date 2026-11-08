variable "environment" {
  type = string
}

variable "project_name" {
  type = string
}

variable "tables" {
  type = list(object({
    name                   = string
    hash_key               = string
    hash_key_type          = string
    range_key              = optional(string)
    range_key_type         = optional(string)
    billing_mode           = optional(string)
    read_capacity          = optional(number)
    write_capacity         = optional(number)
    stream_enabled         = optional(bool)
    stream_view_type       = optional(string)
    ttl_enabled            = optional(bool)
    ttl_attribute_name     = optional(string)
    deletion_protection    = optional(bool)
    point_in_time_recovery = optional(bool)
    global_secondary_indexes = optional(list(object({
      name            = string
      hash_key        = string
      hash_key_type   = string
      range_key       = optional(string)
      range_key_type  = optional(string)
      projection_type = optional(string)
      read_capacity   = optional(number)
      write_capacity  = optional(number)
    })), [])
  }))
}

variable "kms_key_arn" {
  type    = string
  default = null
}

variable "tags" {
  type = map(string)
}
