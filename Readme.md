# Spring Cloud GitHub
<hr/>

To load properties directly from the GitHub Repository without a Configuration Server. <br/>
It is recommended to use it at the time of rapid development of **small projects**.

## Usage
### 1. Set your github tokens.
Using system env
```
export GITHUB_CLOUD_TOKEN=${Your github token}
export GITHUB_CLOUD_URI=${Your github API URI} // Using GitHub Enterprise
```
Or Application properties
```
// application.properties 
github.cloud.token: ${Your github token}
github.cloud.end-point-uri: ${Your github API URI} // Using GitHub Enterprise
```
or Environment variables
```
// run with below variables.
--github.cloud.token=${Your github token}
--github.cloud.end-point-uri=${Your github API URI} // Using GitHub Enterprise
```


### 2. Set Application Properties file
> {path} must specify the path to the file.

```
// application.yaml
spring:
    cloud:
        import: github-cloud/{owner}/{repository}:{path}
```
or
```
// application.yaml
spring:
    cloud:
        import: github-cloud/{owner}/{repository}:{path1};{path2};{path3}
```

