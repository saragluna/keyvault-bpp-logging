### DeferredLog in EnvironmentPostProcessor - Log Issue Demo

This sample demonstrates the issue when Azure SDKs for Java are used in Spring components like `EnvironmentPostProcessor`, which is an earlier stage in Spring context, the logs printed by Azure SDKs cannot be output. 

### Problem Statement
In Spring, a `DeferredLog` is intended to delay logging until the logging system is fully initialized, as what we did in the `KeyVaultEnvironmentPostProcessor` class. But there is still a problem for `SecretClient` built in the `KeyVaultEnvironmentPostProcessor` class. The secret client uses `ClientLogger` and will delegate the logging to any log provider. But at this stage, the log provider is not fully initialized, so any logs output by the `SecretClient` will not be print at all. 

### Reproducing the Issue
1. Create a Key Vault service instance, and assign RBAC role to the user logs in to your local Azure CLI.
2. Update the `endpoint` property in the `application.yaml`.
3. Run or debug the application.
4. Check this code snippet in the `SecretClient`, if the secret name is null, it will try to log and throw the exception. But in our case, it won't be able to log anything


```java
@ServiceMethod(returns = ReturnType.SINGLE)
public Response<KeyVaultSecret> getSecretWithResponse(String name, String version, Context context) {
    if (CoreUtils.isNullOrEmpty(name)) {
        throw LOGGER.logExceptionAsError(new IllegalArgumentException("'name' cannot be null or empty."));
    }

    return callWithMappedException(() -> {
        Response<SecretBundle> response = implClient.getSecretWithResponse(vaultUrl, name, version, context);
        return new SimpleResponse<>(response, createKeyVaultSecret(response.getValue()));
    }, SecretAsyncClient::mapGetSecretException);
}
```
