FROM vote-gateway-base AS builder
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew bootJar --no-daemon

FROM gcr.io/distroless/java21-debian12:nonroot

WORKDIR /app

COPY --from=builder /workspace/build/libs/vote-gateway.jar app.jar

ENV JAVA_TOOL_OPTIONS=""

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
