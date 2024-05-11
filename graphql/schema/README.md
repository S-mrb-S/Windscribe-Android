download new schema file :
./gradlew mobile:downloadApolloSchema \
  --endpoint="https://api.example/graphql/" \
  --schema="graphql/main/schema.graphqls"

And

copy file to :
mobile/src/main/graphql/sp/windscribe/mobile
