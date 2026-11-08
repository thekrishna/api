output "kinesis_stream_names" {
  description = "Names of the configured Kinesis streams."
  value       = module.kinesis.stream_names
}

output "kinesis_stream_arns" {
  description = "ARNs of the configured Kinesis streams."
  value       = module.kinesis.stream_arns
}

output "dynamodb_table_names" {
  description = "Names of the configured DynamoDB tables."
  value       = module.dynamodb.table_names
}

output "dynamodb_table_arns" {
  description = "ARNs of the configured DynamoDB tables."
  value       = module.dynamodb.table_arns
}

output "application_role_arn" {
  description = "ARN of the application IAM role used by FNOL services."
  value       = module.iam.application_role_arn
}

output "cloudwatch_log_group_name" {
  description = "Main CloudWatch log group used for FNOL service logs."
  value       = module.monitoring.log_group_name
}

output "application_configuration" {
  description = "Recommended environment variables for the existing FNOL Java application."
  value = {
    AWS_REGION                   = var.aws_region
    KINESIS_STREAM_FNOL          = var.kinesis_stream_names[0]
    FNOL_CLAIMS_TABLE            = "fnol-claims"
    FNOL_CLAIM_EVENTS_TABLE      = "fnol-claim-events"
    FNOL_DOCUMENTS_TABLE         = "fnol-documents"
    FNOL_PROCESSING_STATUS_TABLE = "fnol-processing-status"
    FNOL_AI_ANALYSIS_TABLE       = "fnol-ai-analysis"
    FNOL_PROCESSED_EVENTS_TABLE  = "fnol-processed-events"
  }
}
