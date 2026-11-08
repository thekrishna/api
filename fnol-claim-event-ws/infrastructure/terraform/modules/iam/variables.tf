variable "project_name" {
  type = string
}

variable "environment" {
  type = string
}

variable "trusted_principals" {
  type = list(string)
}

variable "kinesis_stream_arns" {
  type = map(string)
}

variable "dynamodb_table_arns" {
  type = map(string)
}

variable "tags" {
  type = map(string)
}
