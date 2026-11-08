resource "aws_cloudwatch_log_group" "application" {
  name              = "/aws/fnol/${var.environment}"
  retention_in_days = var.log_retention_days
  tags              = var.tags
}

locals {
  kinesis_alarms = flatten([
    for stream_name in var.kinesis_stream_names : [
      {
        key                = "${stream_name}-IncomingRecords"
        stream_name        = stream_name
        metric_name        = "IncomingRecords"
        namespace          = "AWS/Kinesis"
        threshold          = 100
        period             = 300
        evaluation_periods = 1
      },
      {
        key                = "${stream_name}-IteratorAgeMilliseconds"
        stream_name        = stream_name
        metric_name        = "GetRecords.IteratorAgeMilliseconds"
        namespace          = "AWS/Kinesis"
        threshold          = 5000
        period             = 300
        evaluation_periods = 1
      }
    ]
  ])

  dynamodb_alarms = flatten([
    for table_name in var.dynamodb_table_names : [
      {
        key                = "${table_name}-ThrottledRequests"
        table_name         = table_name
        metric_name        = "ThrottledRequests"
        namespace          = "AWS/DynamoDB"
        threshold          = 0
        period             = 300
        evaluation_periods = 1
      },
      {
        key                = "${table_name}-SystemErrors"
        table_name         = table_name
        metric_name        = "SystemErrors"
        namespace          = "AWS/DynamoDB"
        threshold          = 0
        period             = 300
        evaluation_periods = 1
      }
    ]
  ])
}

resource "aws_cloudwatch_metric_alarm" "kinesis" {
  for_each = { for alarm in local.kinesis_alarms : alarm.key => alarm }

  alarm_name          = "${var.project_name}-${var.environment}-${each.value.key}"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = each.value.evaluation_periods
  metric_name         = each.value.metric_name
  namespace           = each.value.namespace
  period              = each.value.period
  statistic           = "Sum"
  threshold           = each.value.threshold
  alarm_description   = "Alarm for ${each.value.stream_name} ${each.value.metric_name}"
  dimensions = {
    StreamName = each.value.stream_name
  }
  alarm_actions = var.sns_topic_arn != null ? [var.sns_topic_arn] : []
  ok_actions    = var.sns_topic_arn != null ? [var.sns_topic_arn] : []
  tags          = var.tags
}

resource "aws_cloudwatch_metric_alarm" "dynamodb" {
  for_each = { for alarm in local.dynamodb_alarms : alarm.key => alarm }

  alarm_name          = "${var.project_name}-${var.environment}-${each.value.key}"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = each.value.evaluation_periods
  metric_name         = each.value.metric_name
  namespace           = each.value.namespace
  period              = each.value.period
  statistic           = "Sum"
  threshold           = each.value.threshold
  alarm_description   = "Alarm for ${each.value.table_name} ${each.value.metric_name}"
  dimensions = {
    TableName = each.value.table_name
  }
  alarm_actions = var.sns_topic_arn != null ? [var.sns_topic_arn] : []
  ok_actions    = var.sns_topic_arn != null ? [var.sns_topic_arn] : []
  tags          = var.tags
}
