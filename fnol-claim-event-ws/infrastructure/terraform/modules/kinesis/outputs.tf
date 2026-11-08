output "stream_names" {
  description = "Names of all created Kinesis streams."
  value       = [for stream in aws_kinesis_stream.this : stream.name]
}

output "stream_arns" {
  description = "ARNs of all created Kinesis streams."
  value       = { for k, stream in aws_kinesis_stream.this : k => stream.arn }
}
