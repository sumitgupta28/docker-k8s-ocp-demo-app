#build Image
docker build . -t sumitgupta28/ocp-demo-app-kafka-stream:latest
#docker Login
docker login
#docker Push
docker push sumitgupta28/ocp-demo-app-kafka-stream:latest