module "kinesis" {
  source = "./modules/kinesis"

  project_name       = var.project_name
  environment        = var.environment
  stream_names       = var.kinesis_stream_names
  shard_count        = var.kinesis_shard_count
  retention_period   = var.kinesis_retention_hours
  stream_mode        = var.kinesis_stream_mode
  encryption_enabled = var.kinesis_encryption_enabled
  kms_key_id         = var.kms_key_arn
  tags               = local.default_tags
}

module "dynamodb" {
  source = "./modules/dynamodb"

  environment  = var.environment
  project_name = var.project_name
  tables       = var.dynamodb_tables
  kms_key_arn  = var.kms_key_arn
  tags         = local.default_tags
}

module "iam" {
  source = "./modules/iam"

  project_name        = var.project_name
  environment         = var.environment
  trusted_principals  = var.trusted_principals
  kinesis_stream_arns = module.kinesis.stream_arns
  dynamodb_table_arns = module.dynamodb.table_arns
  tags                = local.default_tags
}

module "monitoring" {
  source = "./modules/monitoring"

  project_name         = var.project_name
  environment          = var.environment
  kinesis_stream_names = module.kinesis.stream_names
  dynamodb_table_names = module.dynamodb.table_names
  log_retention_days   = var.cloudwatch_log_retention_days
  sns_topic_arn        = null
  tags                 = local.default_tags
}
