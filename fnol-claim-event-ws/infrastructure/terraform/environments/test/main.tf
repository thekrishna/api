module "fnol_infra" {
  source = "../.."

  project_name = "fnol"
  environment  = "test"
  aws_region   = "us-east-1"

  kinesis_stream_names    = ["fnol-claim-events", "fnol-document-events", "fnol-processing-events"]
  kinesis_shard_count     = 2
  kinesis_retention_hours = 72
  kinesis_stream_mode     = "PROVISIONED"

  dynamodb_tables = [
    {
      name                   = "fnol-claims"
      hash_key               = "claimId"
      hash_key_type          = "S"
      billing_mode           = "PAY_PER_REQUEST"
      point_in_time_recovery = true
      deletion_protection    = false
      global_secondary_indexes = [
        { name = "policyNumber-index", hash_key = "policyNumber", hash_key_type = "S", projection_type = "ALL", read_capacity = 5, write_capacity = 5 },
        { name = "claimantId-index", hash_key = "claimantId", hash_key_type = "S", projection_type = "ALL", read_capacity = 5, write_capacity = 5 },
        { name = "claimStatus-index", hash_key = "claimStatus", hash_key_type = "S", projection_type = "ALL", read_capacity = 5, write_capacity = 5 }
      ]
    },
    {
      name                   = "fnol-claim-events"
      hash_key               = "claimId"
      hash_key_type          = "S"
      range_key              = "eventTimestampEventId"
      range_key_type         = "S"
      billing_mode           = "PAY_PER_REQUEST"
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
      point_in_time_recovery = true
      deletion_protection    = false
    },
    {
      name                   = "fnol-processing-status"
      hash_key               = "claimId"
      hash_key_type          = "S"
      billing_mode           = "PAY_PER_REQUEST"
      point_in_time_recovery = true
      deletion_protection    = false
      global_secondary_indexes = [
        { name = "currentStatus-index", hash_key = "currentStatus", hash_key_type = "S", projection_type = "ALL", read_capacity = 5, write_capacity = 5 }
      ]
    },
    {
      name                   = "fnol-ai-analysis"
      hash_key               = "claimId"
      hash_key_type          = "S"
      range_key              = "analysisId"
      range_key_type         = "S"
      billing_mode           = "PAY_PER_REQUEST"
      point_in_time_recovery = true
      deletion_protection    = false
    },
    {
      name                   = "fnol-processed-events"
      hash_key               = "eventId"
      hash_key_type          = "S"
      billing_mode           = "PAY_PER_REQUEST"
      point_in_time_recovery = true
      deletion_protection    = false
    }
  ]

  cloudwatch_log_retention_days = 90
  enable_dead_letter_queue      = true
  trusted_principals            = ["ecs-tasks.amazonaws.com", "ec2.amazonaws.com"]
}
