variable "project_name" {
  type        = string
  default     = "fnol"
  description = "Logical project name used as the prefix for AWS resource names."
}

variable "environment" {
  type        = string
  default     = "dev"
  description = "Deployment environment name."

  validation {
    condition     = contains(["dev", "test", "prod"], lower(var.environment))
    error_message = "environment must be one of: dev, test, prod."
  }
}

variable "aws_region" {
  type        = string
  default     = "us-east-1"
  description = "AWS region to deploy infrastructure into."
}

variable "use_localstack" {
  type        = bool
  default     = false
  description = "Whether to configure the AWS provider to point at LocalStack instead of real AWS."
}

variable "localstack_endpoint" {
  type        = string
  default     = "http://localhost:4566"
  description = "LocalStack endpoint used when use_localstack is true."
}

variable "kinesis_stream_names" {
  type        = list(string)
  default     = ["fnol-claim-events", "fnol-document-events", "fnol-processing-events"]
  description = "Kinesis streams required by the existing FNOL application."
}

variable "kinesis_shard_count" {
  type        = number
  default     = 1
  description = "Default shard count for provisioned Kinesis streams."
}

variable "kinesis_retention_hours" {
  type        = number
  default     = 24
  description = "Retention period for Kinesis streams in hours."
}

variable "kinesis_stream_mode" {
  type        = string
  default     = "PROVISIONED"
  description = "Kinesis stream mode. Use PROVISIONED for stable workloads and ON_DEMAND for bursty workloads."

  validation {
    condition     = contains(["PROVISIONED", "ON_DEMAND"], upper(var.kinesis_stream_mode))
    error_message = "kinesis_stream_mode must be PROVISIONED or ON_DEMAND."
  }
}

variable "kinesis_encryption_enabled" {
  type        = bool
  default     = true
  description = "Whether to enable KMS encryption for Kinesis streams."
}

variable "kms_key_arn" {
  type        = string
  default     = null
  description = "Optional KMS key ARN to use for Kinesis and DynamoDB encryption."
}

variable "dynamodb_tables" {
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
  default = [
    {
      name                   = "fnol-claims"
      hash_key               = "claimId"
      hash_key_type          = "S"
      billing_mode           = "PAY_PER_REQUEST"
      stream_enabled         = false
      ttl_enabled            = false
      point_in_time_recovery = true
      deletion_protection    = true
      global_secondary_indexes = [
        {
          name            = "policyNumber-index"
          hash_key        = "policyNumber"
          hash_key_type   = "S"
          projection_type = "ALL"
          read_capacity   = 5
          write_capacity  = 5
        },
        {
          name            = "claimantId-index"
          hash_key        = "claimantId"
          hash_key_type   = "S"
          projection_type = "ALL"
          read_capacity   = 5
          write_capacity  = 5
        },
        {
          name            = "claimStatus-index"
          hash_key        = "claimStatus"
          hash_key_type   = "S"
          projection_type = "ALL"
          read_capacity   = 5
          write_capacity  = 5
        }
      ]
    },
    {
      name                   = "fnol-claim-events"
      hash_key               = "claimId"
      hash_key_type          = "S"
      range_key              = "eventTimestampEventId"
      range_key_type         = "S"
      billing_mode           = "PAY_PER_REQUEST"
      stream_enabled         = false
      ttl_enabled            = false
      point_in_time_recovery = true
      deletion_protection    = false
    },
    {
      name                   = "fnol-documents"
      hash_key               = "claimId"
      hash_key_type          = "S"
      range_key              = "documentId"
      range_key_type         = "S"
      billing_mode           = "PAY_PER_REQUEST"
      stream_enabled         = false
      ttl_enabled            = false
      point_in_time_recovery = true
      deletion_protection    = false
    },
    {
      name                   = "fnol-processing-status"
      hash_key               = "claimId"
      hash_key_type          = "S"
      billing_mode           = "PAY_PER_REQUEST"
      stream_enabled         = false
      ttl_enabled            = false
      point_in_time_recovery = true
      deletion_protection    = false
      global_secondary_indexes = [
        {
          name            = "currentStatus-index"
          hash_key        = "currentStatus"
          hash_key_type   = "S"
          projection_type = "ALL"
          read_capacity   = 5
          write_capacity  = 5
        }
      ]
    },
    {
      name                   = "fnol-ai-analysis"
      hash_key               = "claimId"
      hash_key_type          = "S"
      range_key              = "analysisId"
      range_key_type         = "S"
      billing_mode           = "PAY_PER_REQUEST"
      stream_enabled         = false
      ttl_enabled            = false
      point_in_time_recovery = true
      deletion_protection    = false
    },
    {
      name                   = "fnol-processed-events"
      hash_key               = "eventId"
      hash_key_type          = "S"
      billing_mode           = "PAY_PER_REQUEST"
      stream_enabled         = false
      ttl_enabled            = false
      point_in_time_recovery = true
      deletion_protection    = false
    }
  ]
  description = "All DynamoDB tables required by the FNOL event-driven application."
}

variable "cloudwatch_log_retention_days" {
  type        = number
  default     = 30
  description = "Retention period for CloudWatch log groups."
}

variable "enable_dead_letter_queue" {
  type        = bool
  default     = false
  description = "Whether to create an SQS dead-letter queue for failed Kinesis processing."
}

variable "dlq_name" {
  type        = string
  default     = "fnol-event-dlq"
  description = "Name of the optional SQS dead-letter queue."
}

variable "trusted_principals" {
  type        = list(string)
  default     = ["ecs-tasks.amazonaws.com", "ec2.amazonaws.com"]
  description = "Principal services allowed to assume the FNOL application role."
}

variable "tags" {
  type        = map(string)
  default     = {}
  description = "Additional tags applied to all resources."
}
