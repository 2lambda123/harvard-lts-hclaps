# Release

## Maven Central

Pre-reqs:

1. A Sonatype account with access to the LTS group
2. GPG
3. Maven setup with your Sonatype and GPG credentials: https://central.sonatype.org/publish/publish-maven/

Execute the following to publish to central:

```shell
mvn -Prelease-central clean deploy
```

Note:

1. The above command will **not** modify the version. You should use either `mvn release` or `mvn versions:set` to
set the version prior to publishing.
2. The plugin is currently configured to stage the artifact in Central, but **not** release it. To release it, you
must sign in at https://s01.oss.sonatype.org/ and follow [these instructions](https://central.sonatype.org/publish/release/#performing-a-release-deployment-with-the-maven-release-plugin).
