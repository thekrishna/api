variable "project_name" {
  type = string
}

variable "environment" {
  type = string
}

variable "stream_names" {
  type = list(string)
}

variable "shard_count" {
  type = number
}

variable "retention_period" {
  type = number
}

variable "stream_mode" {
  type = string
}

variable "encryption_enabled" {
  type = bool
}

variable "kms_key_id" {
  type    = string
  default = null
}

variable "tags" {
  type = map(string)
}
