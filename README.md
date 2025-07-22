# TraceIP

Application for retrieving the following country information given an `IP address`:
- Country (with ISO code)
- Languages
- Currency
- Times (more than one if country with many timezones)
- Estimated distance to Buenos Aires. 

## Build and Test 🏗️
- IDE: [Intellij](https://www.jetbrains.com/idea/)
- Build tool: [Maven](https://maven.apache.org/) 
- Test framework: [JUnit 5](https://junit.org/)

### Keep it green! 🍃 
- Configured [GitHub action](https://github.com/frt-gh01/traceip/actions/workflows/maven.yml) for running tests on push to main.

## Dockerized ⛴️

### Build image

```shell
$ docker build -t traceip .
```

### Run container (and delete after run)

Example run with IP `192.168.0.1`:

```shell
$ docker run --rm traceip -t 192.168.0.1
```
