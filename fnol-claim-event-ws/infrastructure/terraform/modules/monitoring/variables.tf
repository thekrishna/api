variable "project_name" {
  type = string
}

variable "environment" {
  type = string
}

variable "kinesis_stream_names" {
  type = list(string)
}

variable "dynamodb_table_names" {
  type = list(string)
}

variable "log_retention_days" {
  type = number
}

variable "sns_topic_arn" {
  type    = string
  default = null
}

variable "tags" {
  type = map(string)
}
