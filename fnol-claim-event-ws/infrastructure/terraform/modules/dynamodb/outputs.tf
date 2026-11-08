output "table_names" {
  description = "Names of all created DynamoDB tables."
  value       = [for table in aws_dynamodb_table.this : table.name]
}

output "table_arns" {
  description = "ARNs of all created DynamoDB tables."
  value       = { for k, table in aws_dynamodb_table.this : k => table.arn }
}
