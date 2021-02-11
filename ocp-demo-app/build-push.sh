#build Image
docker build . -t sumitgupta28/ocp-demo-app:latest
#docker Login
docker login
#docker Push
docker push sumitgupta28/ocp-demo-app:latest
