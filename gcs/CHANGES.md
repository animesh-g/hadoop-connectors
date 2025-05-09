# Release Notes

## Next

1. Add getFileStatusWithHint() API

1. Upgrade google-cloud-storage to 2.43.1

1. Upgrade grpc to 1.68.0

1. Add gRPC configuration documentation

1.  Remove Hadoop 2.x support.

1.  Update all dependencies to the latest versions.

1.  Add support for downscoped tokens in `AccessTokenProvider`.

1.  Implement `FileSystem.openFile` to take advantage of the `FileStatus` if
    passed.

1.  Remove an obsolete `AuthorizationHandler` and related properties:

    ```
    fs.gs.authorization.handler.impl
    fs.gs.authorization.handler.properties.<AUTHORIZATION_HANDLER_PROPERTY>
    ```

1.  Remove support for Apache HTTP transport and related property:

    ```
    fs.gs.http.transport.type
    ```

1.  Support GCS fine-grained action in AuthorizationHandlers.

1.  Decrease log level for `hflush` rate limit log message.

1.  Remove Cooperative Locking support for directory operations and related
    properties:

    ```
    fs.gs.cooperative.locking.enable
    fs.gs.cooperative.locking.expiration.timeout.ms
    fs.gs.cooperative.locking.max.concurrent.operations
    ```

1.  Migrate authentication to `com.google.auth.Credentials` and remove obsolete
    properties:

    ```
    fs.gs.auth.service.account.email
    fs.gs.auth.service.account.keyfile
    fs.gs.auth.service.account.private.key
    fs.gs.auth.service.account.private.key.id
    ```

1.  Refactor authentication configuration to use an explicit `fs.gs.auth.type`
    enum property, instead of relying on inference of the authentication type
    based on the set configuration properties, and remove obsolete properties:

    ```
    fs.gs.auth.null.enable
    fs.gs.auth.service.account.enable
    ```

1.  Add support for a new `USER_CREDENTIALS` authentication type that retrieves
    a refresh token using the authorisation code grant flow configured via the
    following properties:

    ```
    fs.gs.auth.client.id
    fs.gs.auth.client.secret
    fs.gs.auth.refresh.token
    ```

1.  Merge all output stream types functionality in the default output stream
    that behaves similarly to the `FLUSHABLE_COMPOSITE` stream, and remove
    obsolete `fs.gs.outputstream.type` property.

1.  Set default value for `fs.gs.list.max.items.per.call` property to `5000`.

1.  Set socket read timeout (`fs.gs.http.read-timeout`) as early as possible on
    new sockets returned from the custom `SSLSocketFactory`. This guarantees the
    timeout is enforced during TLS handshakes when using Conscrypt as the
    security provider.

1.  The Google Cloud Storage Connector now can be used as a
    [Hadoop Credential Provider](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/CredentialProviderAPI.html).

1.  Added dependency on the Cloud Storage Client Library
    ([google-cloud-storage](https://github.com/googleapis/java-storage/tree/main/google-cloud-storage)).

1.  Rename `fs.gs.rewrite.max.bytes.per.call` property to
    `fs.gs.rewrite.max.chunk.size`.

1.  Remove support of the deprecated `fs.gs.io.buffersize.write` property.

1.  Add support for size suffixes (`k`, `m`, `g`, etc) in values of size-related
    properties:

    ```
    fs.gs.inputstream.inplace.seek.limit
    fs.gs.inputstream.min.range.request.size
    fs.gs.outputstream.buffer.size
    fs.gs.outputstream.pipe.buffer.size
    fs.gs.outputstream.upload.cache.size
    fs.gs.outputstream.upload.chunk.size
    fs.gs.rewrite.max.chunk.size
    ```

1.  Remove `.ms` suffix from names and add support for time suffixes (`ms`, `s`,
    `m`, etc) in values of time-related properties:

    ```
    fs.gs.http.connect-timeout
    fs.gs.http.read-timeout
    fs.gs.max.wait.for.empty.object.creation
    fs.gs.outputstream.sync.min.interval
    fs.gs.performance.cache.max.entry.age
    ```

1.  Change default values of properties:

    ```
    fs.gs.http.connect-timeout (default: 20s -> 5s)
    fs.gs.http.read-timeout (default: 20s -> 5s)
    fs.gs.outputstream.upload.chunk.size (default: 64m -> 24m)
    ```

1.  Upgrade Hadoop to 3.3.5.

1.  Upgrade java-storage to 2.29.0

1.  Add support for `WORKLOAD_IDENTITY_FEDERATION_CREDENTIAL_CONFIG_FILE` authentication type that retrieves a refresh token using workload identity federation configuraiton defined in: `fs.gs.auth.workload.identity.federation.credential.config.file`

### 2.2.2 - 2021-06-25

1.  Support footer prefetch in gRPC read channel.

1.  Fix in-place seek functionality in gRPC read channel.

1.  Add option to buffer requests for resumable upload over gRPC:

    ```
    fs.gs.grpc.write.buffered.requests (default : 20)
    ```

### 2.2.1 - 2021-05-04

1.  Fix proxy configuration for Apache HTTP transport.

1.  Update gRPC dependency to latest version.

### 2.2.0 - 2021-01-06

1.  Delete deprecated methods.

1.  Update all dependencies to latest versions.

1.  Add support for Cloud Storage objects CSEK encryption:

    ```
    fs.gs.encryption.algorithm (not set by default)
    fs.gs.encryption.key (not set by default)
    fs.gs.encryption.key.hash (not set by default)
    ```

1.  Add a property to override storage service path:

    ```
    fs.gs.storage.service.path (default: `storage/v1/`)
    ```

1.  Added a new output stream type which can be used by setting:

    ```
    fs.gs.outputstream.type=FLUSHABLE_COMPOSITE
    ```

    The `FLUSHABLE_COMPOSITE` output stream type behaves similarly to the
    `SYNCABLE_COMPOSITE` type, except it also supports `hflush()`, which uses
    the same implementation with `hsync()` in the `SYNCABLE_COMPOSITE` output
    stream type.

1.  Added a new output stream parameter

    ```
    fs.gs.outputstream.sync.min.interval.ms (default: 0)
    ```

    to configure the minimum time interval (milliseconds) between consecutive
    syncs. This is to avoid getting rate limited by GCS. Default is `0` - no
    wait between syncs. `hsync()` when rate limited will block on waiting for
    the permits, but `hflush()` will simply perform nothing and return.

1.  Added a new parameter to configure output stream pipe type:

    ```
    fs.gs.outputstream.pipe.type (default: IO_STREAM_PIPE)
    ```

    Valid values are `NIO_CHANNEL_PIPE` and `IO_STREAM_PIPE`.

    Output stream now supports (when property value set to `NIO_CHANNEL_PIPE`)
    [Java NIO Pipe](https://docs.oracle.com/javase/8/docs/api/java/nio/channels/Pipe.html)
    that allows to reliably write in the output stream from multiple threads
    without *"Pipe broken"* exceptions.

    Note that when using `NIO_CHANNEL_PIPE` option maximum upload throughput can
    decrease by 10%.

1.  Add a property to impersonate a service account:

    ```
    fs.gs.auth.impersonation.service.account (not set by default)
    ```

    If this property is set, an access token will be generated for this service
    account to access GCS. The caller who issues a request for the access token
    must have been granted the Service Account Token Creator role
    (`roles/iam.serviceAccountTokenCreator`) on the service account to
    impersonate.

1.  Throw `ClosedChannelException` in `GoogleHadoopOutputStream.write` methods
    if stream already closed. This fixes Spark Streaming jobs checkpointing to
    Cloud Storage.

1.  Add properties to impersonate a service account through user or group name:

    ```
    fs.gs.auth.impersonation.service.account.for.user.<USER_NAME> (not set by default)
    fs.gs.auth.impersonation.service.account.for.group.<GROUP_NAME> (not set by default)
    ```

    If any of these properties are set, an access token will be generated for
    the service account associated with specified username or group name in
    order to access GCS. The caller who issues a request for the access token
    must have been granted the Service Account Token Creator role
    (`roles/iam.serviceAccountTokenCreator`) on the service account to
    impersonate.

1.  Fix complex patterns globbing.

1.  Added support for an authorization handler for Cloud Storage requests. This
    feature is configurable through the properties:

    ```
    fs.gs.authorization.handler.impl=<FULLY_QUALIFIED_AUTHORIZATION_HANDLER_CLASS>
    fs.gs.authorization.handler.properties.<AUTHORIZATION_HANDLER_PROPERTY>=<VALUE>
    ```

    If the `fs.gs.authorization.handler.impl` property is set, the specified
    authorization handler will be used to authorize Cloud Storage API requests
    before executing them. The handler will throw `AccessDeniedException` for
    rejected requests if user does not have enough permissions (not authorized)
    to execute these requests.

    All properties with the `fs.gs.authorization.handler.properties.` prefix
    passed to an instance of the configured authorization handler class after
    instantiation before calling any Cloud Storage requests handling methods.

1.  Set default value for `fs.gs.status.parallel.enable` property to `true`.

1.  Tune exponential backoff configuration for Cloud Storage requests.

1.  Increment Hadoop `FileSystem.Statistics` counters for read and write
    operations.

1.  Always infer implicit directories and remove
    `fs.gs.implicit.dir.infer.enable` property.

1.  Replace 2 glob-related properties (`fs.gs.glob.flatlist.enable` and
    `fs.gs.glob.concurrent.enable`) with a single property to configure glob
    search algorithm:

    ```
    fs.gs.glob.algorithm (default: CONCURRENT)
    ```

1.  Do not create the parent directory objects (this includes buckets) when
    creating a new file or a directory, instead rely on the implicit directory
    inference.

1.  Use default logging backend for Google Flogger instead of Slf4j.

1.  Add `FsBenchmark` tool for benchmarking HCFS.

1.  Remove obsolete `fs.gs.inputstream.buffer.size` property and related
    functionality.

1.  Fix unauthenticated access support (`fs.gs.auth.null.enable=true`).

1.  Improve cache hit ratio when `fs.gs.performance.cache.enable` property is
    set to `true`.

1.  Remove obsolete configuration properties and related functionality:

    ```
    fs.gs.auth.client.id
    fs.gs.auth.client.file
    fs.gs.auth.client.secret
    ```

1.  Add a property that allows to disable HCFS semantic enforcement. If set to
    `false` GSC connector will not check if directory with same name already
    exists when creating a new file and vise versa.

    ```
    fs.gs.create.items.conflict.check.enable (default: true)
    ```

1.  Remove redundant properties:

    ```
    fs.gs.config.override.file
    fs.gs.copy.batch.threads
    fs.gs.copy.max.requests.per.batch
    ```

1.  Change default value of `fs.gs.inputstream.min.range.request.size` property
    from `524288` to `2097152`.

1.  Add readVectored API implementation.

### 2.1.1 - 2020-03-11

1.  Add upload cache to support high-level retries of failed uploads. Cache size
    configured via property and disabled by default (zero or negative value):

    ```
    fs.gs.outputstream.upload.cache.size (deafult: 0)
    ```

### 2.1.0 - 2020-03-09

1.  Update all dependencies to latest versions.

1.  Use `storage.googleapis.com` API endpoint.

1.  Fix proxy authentication when using `JAVA_NET` transport.

1.  Remove Log4j backend for Google Flogger.

1.  Add properties to override Google Cloud API endpoints:

    ```
    fs.gs.storage.root.url (default: https://storage.googleapis.com/)
    fs.gs.token.server.url (default: https://oauth2.googleapis.com/token)
    ```

1.  Support adding custom HTTP headers to Cloud Storage API requests:

    ```
    fs.gs.storage.http.headers.<HEADER>=<VALUE> (not set by default)
    ```

    Example:

    ```
    fs.gs.storage.http.headers.some-custom-header=custom_value
    fs.gs.storage.http.headers.another-custom-header=another_custom_value
    ```

1.  Always set `generation` parameter for read requests and remove
    `fs.gs.generation.read.consistency` property.

1.  Always use URI path encoding and remove `fs.gs.path.encoding` property.

1.  Use Slf4j backend by default for Google Flogger.

1.  Remove list requests caching in the `PerformanceCachingGoogleCloudStorage`
    and `fs.gs.performance.cache.list.caching.enable` property.

1.  Stop caching non-existent (not found) items in performance cache.

### 2.0.1 - 2020-02-13

1.  Cooperative Locking FSCK tool: fix recovery of operations that failed before
    creating an operation log file.

1.  Change Gson dependency scope from `provided` to `compile` in `gcsio`
    library.

### 2.0.0 - 2019-08-23

1.  Remove Hadoop 1.x support.

1.  Do not convert path to directory path for inferred implicit directories.

1.  Do not parallelize GCS list requests, because it leads to too high QPS.

1.  Fix bug when GCS connector lists all files in directory instead of specified
    limit.

1.  Eagerly initialize `GoogleCloudStorageReadChannel` metadata if
    `fs.gs.inputstream.fast.fail.on.not.found.enable` set to true.

1.  Add support for Hadoop Delegation Tokens (based on
    [HADOOP-14556](https://issues.apache.org/jira/browse/HADOOP-14556)).
    Configurable via `fs.gs.delegation.token.binding` property.

1.  Remove obsolete `fs.gs.file.size.limit.250gb` property.

1.  Repair implicit directories during delete and rename operations instead of
    list and glob operations.

1.  Log HTTP `429 Too Many Requests` responses from GCS at 1 per 10 seconds
    rate.

1.  Remove obsolete `fs.gs.create.marker.files.enable` property.

1.  Remove system bucket feature and related properties:

    ```
    fs.gs.system.bucket
    fs.gs.system.bucket.create
    ```

1.  Remove obsolete `fs.gs.performance.cache.dir.metadata.prefetch.limit`
    property.

1.  Add a property to parallelize GCS requests in `getFileStatus` and
    `listStatus` methods to reduce latency:

    ```
    fs.gs.status.parallel.enable (default: false)
    ```

    Setting this property to `true` will cause GCS connector to send more GCS
    requests which will decrease latency but also increase cost of
    `getFileStatus` and `listStatus` method calls.

1.  Add a property to enable GCS direct upload:

    ```
    fs.gs.outputstream.direct.upload.enable (default: false)
    ```

1.  Update all dependencies to latest versions.

1.  Support Cooperative Locking for directory operations:

    ```
    fs.gs.cooperative.locking.enable (default: false)
    fs.gs.cooperative.locking.expiration.timeout.ms (default: 120,000)
    fs.gs.cooperative.locking.max.concurrent.operations (default: 20)
    ```

1.  Add FSCK tool for recovery of failed Cooperative Locking for directory
    operations:

    ```
    hadoop jar /usr/lib/hadoop/lib/gcs-connector.jar \
        com.google.cloud.hadoop.fs.gcs.CoopLockFsck \
        --{check,rollBack,rollForward} gs://<bucket_name> [all|<operation_id>]
    ```

1.  Implement Hadoop File System `append` method using GCS compose API.

1.  Disable support for reading GZIP encoded files (HTTP header
    `Content-Encoding: gzip`) because processing of
    [GZIP encoded](https://cloud.google.com/storage/docs/transcoding#decompressive_transcoding)
    files is inefficient and error-prone in Hadoop and Spark.

    This feature is configurable with the property:

    ```
    fs.gs.inputstream.support.gzip.encoding.enable (default: false)
    ```

1.  Remove parent directory timestamp update feature and related properties:

    ```
    fs.gs.parent.timestamp.update.enable
    fs.gs.parent.timestamp.update.substrings.excludes
    fs.gs.parent.timestamp.update.substrings.includes
    ```

    This feature was enabled by default only for job history files, but it's not
    necessary anymore for Job History Server to work properly after
    [MAPREDUCE-7101](https://issues.apache.org/jira/browse/MAPREDUCE-7101).

### 1.9.14 - 2019-02-13

1.  Implement Hadoop File System `concat` method using GCS compose API.

1.  Add Hadoop File System extended attributes support.

### 1.9.13 - 2019-02-04

1.  Fix implicit directories inference.

### 1.9.12 - 2019-01-30

1.  Update all dependencies to latest versions.

1.  Improve GCS IO exception messages.

1.  Reduce latency of GCS IO operations.

1.  Fix bug that could lead to data duplication when reading files with GZIP
    content encoding (HTTP header `Content-Encoding: gzip`) that have
    uncompressed size of more than 2.14 GiB.

### 1.9.11 - 2018-12-20

1.  Changed the default value of `fs.gs.path.encoding` to `uri-path`, the new
    codec introduced in 1.4.5. The old behavior can be restored by setting
    `fs.gs.path.encoding` to `legacy`.

1.  Update all dependencies to latest versions.

1.  Don't use `fs.gs.performance.cache.dir.metadata.prefetch.limit` property to
    prefetch metadata in `PerformanceCachingGoogleCloudStorage` - always use
    single objects list request, because prefetching metadata with multiple list
    requests (when directory contains a lot of files) could introduce
    performance penalties when using performance cache.

1.  Add an option to lazily initialize `GoogleHadoopFileSystem` instances:

    ```
    fs.gs.lazy.init.enable (default: false)
    ```

1.  Add ability to unset `fs.gs.system.bucket` with an empty string value:

    ```
    fs.gs.system.bucket=
    ```

1.  Set default value for `fs.gs.working.dir` property to `/`.

### 1.9.10 - 2018-11-01

1.  Use Hadoop `CredentialProvider` API to retrieve proxy credentials.

1.  Remove 1024 compose components limit from `SYNCABLE_COMPOSITE` output stream
    type.

### 1.9.9 - 2018-10-19

1.  Add an option for running flat and regular glob search algorithms in
    parallel:

    ```
    fs.gs.glob.concurrent.enable (default: true)
    ```

    Returns a result of an algorithm that finishes first and cancels the other
    algorithm.

1.  Add an option to provide path for configuration override file:

    ```
    fs.gs.config.override.file (default: null)
    ```

    Connector overrides its configuration with values provided in this file.
    This file should be in XML format that follows the same schema as Hadoop
    configuration files.

### 1.9.8 - 2018-10-03

1.  Expose `FileChecksum` in `GoogleHadoopFileSystem` via property:

    ```
    fs.gs.checksum.type (default: NONE)
    ```

    Valid values: `NONE`, `CRC32C`, `MD5`.

    CRC32c checksum is compatible with
    [HDFS-13056](https://issues.apache.org/jira/browse/HDFS-13056).

1.  Add support for proxy authentication for both `APACHE` and `JAVA_NET`
    `HttpTransport` options.

    Proxy authentication is configurable with properties:

    ```
    fs.gs.proxy.username (default: null)
    fs.gs.proxy.password (default: null)
    ```

1.  Update Apache HttpClient to the latest version.

### 1.9.7 - 2018-09-20

1.  Add an option to provide credentials directly in Hadoop Configuration,
    without having to place a file on every node, or associating service
    accounts with GCE VMs:

    ```
    fs.gs.auth.service.account.private.key.id
    fs.gs.auth.service.account.private.key
    ```

1.  Add an option to specify max bytes rewritten per rewrite request when
    `fs.gs.copy.with.rewrite.enable` is set to `true`:

    ```
    fs.gs.rewrite.max.bytes.per.call (default: 536870912)
    ```

    Even though GCS does not require this parameter for rewrite requests,
    rewrite requests are flaky without it.

### 1.9.6 - 2018-08-30

1.  Change default values for GCS batch/directory operations properties to
    improve performance:

    ```
    fs.gs.copy.max.requests.per.batch (default: 1 -> 15)
    fs.gs.copy.batch.threads (default: 50 -> 15)
    fs.gs.max.requests.per.batch (default: 25 -> 15)
    fs.gs.batch.threads (default: 25 -> 15)
    ```

1.  Migrate logging to Google Flogger.

    To configure Log4j as a Flogger backend set `flogger.backend_factory` system
    property to
    `com.google.common.flogger.backend.log4j.Log4jBackendFactory#getInstance` or
    `com.google.cloud.hadoop.repackaged.gcs.com.google.common.flogger.backend.log4j.Log4jBackendFactory#getInstance`
    if using shaded jar.

    For example:

    ```
    java -Dflogger.backend_factory=com.google.common.flogger.backend.log4j.Log4jBackendFactory#getInstance ...
    ```

1.  Delete read buffer in `GoogleHadoopFSInputStream` class and remove property
    that enables it:

    ```
    fs.gs.inputstream.internalbuffer.enable (default: false)
    ```

1.  Disable read buffer in `GoogleCloudStorageReadChannel` by default because it
    does not provide significant performance benefits:

    ```
    fs.gs.io.buffersize (default: 8388608 -> 0)
    ```

1.  Add configuration properties for buffers in `GoogleHadoopOutputStream`:

    ```
    fs.gs.outputstream.buffer.size (default: 8388608)
    fs.gs.outputstream.pipe.buffer.size (default: 1048576)
    ```

1.  Deprecate and replace properties with new one:

    ```
    fs.gs.io.buffersize -> fs.gs.inputstream.buffer.size (default: 0)
    fs.gs.io.buffersize.write -> fs.gs.outputstream.upload.chunk.size (default: 67108864)
    ```

1.  Enable fadvise `AUTO` mode by default:

    ```
    fs.gs.inputstream.fadvise (default: SEQUENTIAL -> AUTO)
    ```

1.  Update all dependencies to latest versions.

### 1.9.5 - 2018-08-09

1.  Improve build configuration (`pom.xml`s) compatibility with Maven release
    plugin.

    Changes version string from `1.9.5-hadoop2` to `hadoop2-1.9.5`.

1.  Update Maven plugins versions.

1.  Do not send batch request when performing operations (rename, delete, copy)
    on 1 object.

1.  Add `fs.gs.performance.cache.dir.metadata.prefetch.limit` (default: `1000`)
    configuration property to control number of prefetched metadata objects in
    the same directory by `PerformanceCachingGoogleCloudStorage`.

    To disable metadata prefetching set property value to `0`.

    To prefetch all objects metadata in a directory set property value to `-1`.

1.  Add configuration properties to control batching of copy operations
    separately from other operations:

    ```
    fs.gs.copy.max.requests.per.batch (default: 30)
    fs.gs.copy.batch.threads (default: 0)
    ```

1.  Fix `RejectedExecutionException` during parallel execution of GCS batch
    requests.

1.  Change default values for GCS batch/directory operations properties:

    ```
    fs.gs.copy.with.rewrite.enable (default: false -> true)
    fs.gs.copy.max.requests.per.batch (default: 30 -> 1)
    fs.gs.copy.batch.threads (default: 0 -> 50)
    fs.gs.max.requests.per.batch (default: 30 -> 25)
    fs.gs.batch.threads (default: 0 -> 25)
    ```

### 1.9.4 - 2018-08-07

1.  Add `fs.gs.generation.read.consistency (default: LATEST)` property to
    determine read consistency across different generations of a GCS object.

    Three modes are supported:

    *   `LATEST`: this is the default behavior. The connector will ignore
        generation ID of the GCS objects and always try to read the live
        version.

    *   `BEST_EFFORT`: The connector will try to read the generation determined
        when the `GoogleCloudStorageReadChannel` is first established. However,
        if that generation cannot be found anymore, connector will fall back to
        read the live version. This mode allows to improve the performance by
        requesting the same object generation from GCS. Using this mode
        connector can read changing objects from GCS buckets with disabled
        object versioning without failure.

    *   `STRICT`: The connector will always try to read the generation
        determined when the `GoogleCloudStorageReadChannel` is first
        established, and report error (`FileNotFound`) when that generation
        cannot be found anymore.

    Note that this property will only apply to new streams opened after
    generation is determined. It won't affect read from any streams that are
    already open, pre-fetched footer, or the metadata of the object.

1.  Support parallel execution of GCS batch requests.

    Number of threads to execute batch requests configurable via property:

    ```
    fs.gs.batch.threads (default: 0)
    ```

    If `fs.gs.batch.threads` value is set to 0 then batch requests will be
    executed sequentially by caller thread.

1.  Do not fail-fast when creating `GoogleCloudStorageReadChannel` instance for
    non-existing object to avoid GCS metadata request.

1.  Add property to fail fast with `FileNotFoundException` when calling
    `GoogleCloudStorageImpl#open` method (costs additional GCS metadata
    request):

    ```
    fs.gs.inputstream.fast.fail.on.not.found.enable (default: true)
    ```

1.  Lazily initialize `GoogleCloudStorageReadChannel` metadata after first read
    operation.

1.  Lazily pre-fetch footer in `AUTO` and `RANDOM` fadvise modes when reading
    end of the file using `GoogleCloudStorageReadChannel`.

1.  Delete `fs.gs.inputstream.footer.prefetch.size` property and use
    `fs.gs.inputstream.min.range.request.size` property for determining lazy
    footer prefetch size.

    Because `GoogleCloudStorageReadChannel` makes first read without knowing
    object size it uses heuristic to lazily prefetch at most
    `fs.gs.inputstream.min.range.request.size / 2` bytes before read channel
    position in case this is a footer read. This logic simplifies performance
    tuning and renders `fs.gs.inputstream.footer.prefetch.size` property to be
    obsolete.

1.  Delete unused `fs.gs.inputstream.support.content.encoding.enable` property.

1.  Update all dependencies to latest versions.

### 1.9.3 - 2018-07-25

1.  Ignore `fs.gs.io.buffer` property when determining HTTP range request size
    in fadvise `RANDOM` mode which is used to limit minimum size of HTTP range
    request.

1.  Reuse prefetched footer when reading end of the file.

1.  Always skip in place for gzip-encoded files.

1.  Fix Ivy compatibility - resolve artifact versions in released `pom.xml`
    files.

### 1.9.2 - 2018-07-18

1.  Report the UGI user in FileStatus instead of process owner.

1.  Implement automatic fadvise (adaptive range reads). In this mode, connector
    starts to send bounded range requests instead of streaming range requests
    when reading non gzip encoded files after first backward read or forward
    read for more than `fs.gs.inputstream.inplace.seek.limit` bytes was
    detected.

    To activate this behavior set the property:

    ```
    fs.gs.inputstream.fadvise=AUTO (default: SEQUENTIAL)
    ```

1.  Add an option to prefetch footer when creating
    `GoogleCloudStorageReadChannel` in `AUTO` and `RANDOM` fadvise mode.
    Prefetch size is configured via property:

    ```
    fs.gs.inputstream.footer.prefetch.size (default: 0)
    ```

    This optimization is helpful when reading objects in format that stores
    metadata at the end of the file in footer, like Parquet and ORC.

    Note: for this optimization to work, specified footer prefetch size should
    be greater or equal to an actual metadata size stored in the file footer.

    To disable footer pre-fetching set this property to 0.

1.  Cache objects metadata in `PerformanceCachingGoogleCloudStorage` using GCS
    `ListObjects` requests.

1.  Change default values of properties:

    ```
    fs.gs.inputstream.min.range.request.size (default: 1048576 -> 524288)
    fs.gs.performance.cache.max.entry.age.ms (default: 3000 -> 5000)
    fs.gs.performance.cache.list.caching.enable (default: true -> false)
    ```

1.  Change default OAuth 2.0 token server URL to
    `https://oauth2.googleapis.com/token`.

    Default OAuth 2.0 token server URL could be changed via environment
    variable:

    ```
    GOOGLE_OAUTH_TOKEN_SERVER_URL
    ```

### 1.9.1 - 2018-07-11

1.  Fix `PerformanceCachingGoogleCloudStorage`.

1.  Send only 1 GCS metadata request per `GoogleCloudStorageReadChannel` object
    lifecycle to reduce number of GCS requests when reading objects.

1.  Always fail-fast when creating `GoogleCloudStorageReadChannel` instance for
    non-existing GCS object. Remove property that disables this:

    ```
    fs.gs.inputstream.fast.fail.on.not.found.enable
    ```

1.  For gzip-encoded objects always return `Long.MAX_VALUE` size in
    `GoogleCloudStorageReadChannel.size()` method, until object will be fully
    read. This fixes a bug, when clients that rely on `size` method could stop
    reading object prematurely.

1.  Implement fadvise feature that allows to read objects in random mode in
    addition to sequential mode (current behavior).

    In random mode connector will send bounded range requests (HTTP Range
    header) to GCS which are more efficient in some cases (e.g. reading objects
    in row-columnar file formats like ORC, Parquet, etc).

    Range request size is limited by whatever is greater, `fs.gs.io.buffer` or
    read buffer size passed by client.

    To avoid sending too small range requests (couple bytes) what could happen
    if `fs.gs.io.buffer` is 0 and client passes very small read buffer, min
    range request size is limited to 1 MiB by default. To override this limit
    and set minimum range request size to different value, use property:

    ```
    fs.gs.inputstream.min.range.request.size (default: 1048576)
    ```

    To enable fadvise random mode set property:

    ```
    fs.gs.inputstream.fadvise=RANDOM (default: SEQUENTIAL)
    ```

1.  Do not close GCS read channel when calling
    `GoogleCloudStorageReadChannel.position(long)` method.

1.  Remove property that disables use of `includeTrailingDelimiter` GCS
    parameter after it was verified in production for a while:

    ```
    fs.gs.list.directory.objects.enable
    ```

### 1.9.0 - 2018-06-15

1.  Update all dependencies to latest versions.

1.  Delete metadata cache functionality because Cloud Storage has strong native
    list operation consistency already. Deleted properties:

    ```
    fs.gs.metadata.cache.enable
    fs.gs.metadata.cache.type
    fs.gs.metadata.cache.directory
    fs.gs.metadata.cache.max.age.info.ms
    fs.gs.metadata.cache.max.age.entry.ms
    ```

1.  Decrease default value for max requests per batch from 1,000 to 30.

1.  Make max requests per batch value configurable with property:

    ```
    fs.gs.max.requests.per.batch (default: 30)
    ```

1.  Support Hadoop 3.

1.  Change Maven project structure to be better compatible with IDEs.

1.  Delete deprecated `GoogleHadoopGlobalRootedFileSystem`.

1.  Fix thread leaks that were occurring when YARN log aggregation uploaded logs
    to GCS.

1.  Add interface through which user can directly provide the access token.

1.  Add more retries and error handling in GoogleCloudStorageReadChannel, to
    make it more resilient to network errors; also add a property to allow users
    to specify number of retries on low level GCS HTTP requests in case of
    server errors and I/O errors.

1.  Add properties to allow users to specify connect timeout and read timeout on
    low level GCS HTTP requests.

1.  Include prefix/directory objects metadata into `storage.objects.list`
    requests response to improve performance (i.e. set
    `includeTrailingDelimiter` parameter for `storage.objects.list` GCS requests
    to `true`).

### 1.8.1 - 2018-03-29

1.  Add `AUTO` mode support for Cloud Storage Requester Pays feature.

1.  Add support for using Cloud Storage Rewrite requests for copy operation:

    ```
    fs.gs.copy.with.rewrite.enable (default: false)
    ```

    This allows to copy files between different locations and storage classes.

### 1.8.0 - 2018-03-15

1.  Support GCS Requester Pays feature that could be configured with new
    properties:

    ```
    fs.gs.requester.pays.mode (default: DISABLED)
    fs.gs.requester.pays.project.id (not set by default)
    fs.gs.requester.pays.buckets (not set by default)
    ```

1.  Change relocation package in shaded jar to be connector-specific.

1.  Add support for specifying marker files pattern that should be copied last
    during folder rename operation. Pattern is configured with property:

    ```
    fs.gs.marker.file.pattern
    ```

1.  Min required Java version now is Java 8.

### 1.7.0 - 2018-02-22

1.  Fixed an issue where JSON auth files containing user auth e.g.
    `application_default_credentials.json` does not work with
    `google.cloud.auth.service.account.json.keyfile`

1.  Honor `GOOGLE_APPLICATION_DEFAULT_CREDENTIALS` environment variable. For
    Google Application Default Credentials (but not other defaults).

1.  Make `fs.gs.project.id optional`. It is still required for listing buckets,
    creating buckets, and entire BigQuery connector.

1.  Relocate all dependencies in shaded jar.

1.  Update all dependencies to latest versions.

1.  Disable GCS Metadata Cache by default (e.g. set default value of
    `fs.gs.metadata.cache.enable` property to `false`).

### 1.6.2 - 2017-11-21

1.  Wire HTTP transport settings into Credential logic.

### 1.6.1 - 2017-04-14

1.  Added a polling loop when determining if a createEmptyObjects error can
    safely be ignored and expanded the cases in which we will attempt to
    determine if an empty object already exists.

    Previously, if a rate limiting exception was encountered while creating
    empty objects the connector would issue a single get request for that
    object. If the object exists and is zero length we would consider the
    createEmptyObjects call successful and suppress the rate limit exception.

    The new implementation will poll for the existence of the object, up to a
    user-configurable maximum, and will poll when either a rate limiting error
    occurs or when a 500-level error occurs. The maximum can be configured by
    the following setting:

    ```
    fs.gs.max.wait.for.empty.object.creation.ms
    ```

    Any positive value for this setting will be interpreted to mean "poll for up
    to this many milliseconds before making a final determination". The default
    value will cause a maximum wait of 3 seconds. Polling can be disabled by
    setting this key to 0.

### 1.6.0 - 2016-12-16

1.  Added new `PerformanceCachingGoogleCloudStorage`; unlike the existing
    `CacheSupplementedGoogleCloudStorage` which only serves as an advisory cache
    for enforcement of list consistency, the new optional caching layer is able
    to serving certain metadata and listing requests purely out of a short-lived
    in-memory cache to enhance performance of some workloads. This feature is
    disabled by default, and can be controlled with the config settings:

    ```
    fs.gs.performance.cache.enable=true (default: false)
    fs.gs.performance.cache.list.caching.enable=true (default: false)
    ```

    The first option enables the cache to serve `getFileStatus` requests, while
    the second option additionally enables serving `listStatus`. The duration of
    cache entries can be controlled with:

    ```
    fs.gs.performance.cache.max.entry.age.ms (default: 3000)
    ```

    It is not recommended to always run with this feature enabled; it should be
    used specifically to address cases where frameworks perform redundant
    sequential list/stat operations in a non-distributed manner, and on datasets
    which are not frequently changing. It is additionally advised to validate
    data integrity separately whenever using this feature. There is no
    cooperative cache invalidation between different processes when using this
    feature, so concurrent mutations to a location from multiple clients
    **will** produce inconsistent/stale results if this feature is enabled.

### 1.5.5 - 2016-11-04

1.  Minor refactoring of logic in `CacheSupplementedGoogleCloudStorage` to
    extract a reusable `ForwardingGoogleCloudStorage` that can be used for other
    GCS-delegating implementations.

### 1.5.4 - 2016-10-05

1.  Fixed a bug in `GoogleCloudStorageReadChannel` where multiple in-place seeks
    without any "read" in between could throw a range exception.

1.  Fixed plumbing of `GoogleCloudStorageReadOptions` into the in-memory test
    helpers to improve unittest alignment with integration tests.

1.  Fixed handling of parent timestamp updating when a full URI is provided as a
    path for which timestamps should be updated. This allows specifying
    `gs://bucket/p1/p2/object` instead of simply `p1/p2/object`.

1.  Updated Hadoop 2 dependency to 2.7.2.

1.  Imported full set of Hadoop FileSystem contract tests, except for the
    currently-unsupported Concat and Append tests. Minor changes to pass all the
    tests:

    *   `available()` now throws `ClosedChannelException` if called after
        `close()` instead of returning 0.

    *   `read()` and `seek()` throw `ClosedChannelException` if called after
        `close()` instead of `NullPointerException`.

    *   Out-of-bounds seeks now throw `EOFException` instead of
        `IllegalArgumentException`.

    *   Blocked overwrites throw
        `org.apache.hadoop.fs.FileAlreadyExistsException` instead of generic
        `IOException`.

    *   Deleting root `/` recursively or non-recursively when empty will no
        longer delete the underlying GCS bucket by default. To re-enable
        deletion of GCS buckets when recursively deleting root, set
        `fs.gs.bucket.delete.enable=true`.

### 1.5.3 - 2016-09-21

1.  Misc updates in credential acquisition on GCE.

### 1.5.2 - 2016-08-23

1.  Updated `AbstractGoogleAsyncWriteChannel` to always set the
    `X-Goog-Upload-Desired-Chunk-Granularity` header independently of the
    deprecated `X-Goog-Upload-Max-Raw-Size`; in general this improves
    performance of large uploads.

### 1.5.1 - 2016-07-29

1.  Optimized `InMemoryDirectoryListCache` to use a `TreeMap` internally instead
    of a `HashMap`, since most of its usage is "listing by prefix"; aside from
    some benefits for normal list-supplementation, glob listings with a large
    number of subdirectories should be orders of magnitude faster.

### 1.5.0 - 2016-07-15

1.  Changed the single-argument constructor
    `GoogleCloudStorageFileSystem(GoogleCloudStorage)` to inherit the inner
    `GoogleCloudStorageOptions` from the passed-in `GoogleCloudStorage` rather
    than simply falling back to default GoogleCloudStorageFileSystemOptions.

1.  Changed `RetryHttpInitializer` to treat HTTP code 429 as a retriable error
    with exponential backoff instead of erroring out.

1.  Added a new output stream type which can be used by setting:

    ```
    fs.gs.outputstream.type=SYNCABLE_COMPOSITE
    ```

    With the `SYNCABLE_COMPOSITE` have (limited) support for Hadoop's Syncable
    interface, where calling `hsync()` will commit the data written so far into
    persistent storage without closing the output stream; once a writer calls
    `hsync()`, readers will be able to discover and read the contents of the
    file written up to the last successful `hsync()` call immediately. This
    feature is implemented using "composite objects" in Google Cloud Storage,
    and thus has a current hard limit of `1023` calls to `hsync()` over the
    lifetime of the stream after which subsequent `hsync()` calls will throw a
    `CompositeLimitExceededException`, but will still allow writing additional
    data and closing the stream without losing data despite a failed `hsync()`.

1.  Added several optimizations and options controlling the behavior of the
    `GoogleHadoopFSInputStream`:

    *   Removed internal prepopulated ByteBuffer inside
        GoogleHadoopFSInputStream in favor of non-blocking buffering in the
        lower layer channel; this improves performance for independent small
        reads without noticeable impact on large reads. Old behavior can be
        specified by setting:

        ```
        fs.gs.inputstream.internalbuffer.enable=true (default: false)
        ```

    *   Added option to save an extra metadata GET on first read of a channel if
        objects are known not to use the 'Content-Encoding' header. To use this
        optimization set:

        ```
        fs.gs.inputstream.support.content.encoding.enable=false (default: true)
        ```

    *   Added option to save an extra metadata `GET` on `FileSystem.open(Path)`
        if objects are known to exist already, or the caller is resilient to
        delayed errors for nonexistent objects which occur at read time, rather
        than immediately on open(). To use this optimization set:

        ```
        fs.gs.inputstream.fast.fail.on.not.found.enable=false (default: true)
        ```

    *   Added support for "in-place" small seeks by defining a byte limit where
        forward seeks smaller than the limit will be done by reading/discarding
        bytes rather than opening a brand-new channel. This is set to 8MB by
        default, which is approximately the point where the cost of opening a
        new channel is equal to the cost of reading/discarding the bytes
        in-place. Disable by setting the value to 0, or adjust to other
        thresholds:

        ```
        fs.gs.inputstream.inplace.seek.limit=0 (default: 8388608)
        ```

### 1.4.5 - 2016-03-24

1.  Add support for paths that cannot be parsed by Java's URI.create method.
    This support is off by default, but can be enabled by setting Hadoop
    configuration key `fs.gs.path.encoding` to the string `uri-path`. The
    current behavior, and default value of `fs.gs.path.encoding`, is `legacy`.
    The new path encoding scheme will become the default in a future release.

1.  `VerificationAttributes` are now exposed in `GoogleCloudStorageItemInfo`.
    The current support is limited to reading these attributes from what was
    computed by GCS server side. A future release will add support for
    specifying `VerificationAttributes` in `CreateOptions` when creating new
    objects.

### 1.4.4 - 2016-02-02

1.  Add support for JSON keyfiles via a new configuration key:

    ```
    google.cloud.auth.service.account.json.keyfile
    ```

    This key should point to a file that is available locally to jobs to
    cluster.

### 1.4.3 - 2015-11-12

1.  Minor bug fixes and enhancements.

### 1.4.2 - 2015-09-15

1.  Added checking in `GoogleCloudStorageImpl.createEmptyObject(s)` to handle
    `rateLimitExceeded (429)` errors by fetching the fresh underlying info and
    ignoring the error if the object already exists with the intended metadata
    and size. This fixes an
    [issue](https://github.com/GoogleCloudDataproc/hadoop-connectors/issues/10)
    which mostly affects Spark.

1.  Added logging in `GoogleCloudStorageReadChannel` for high-level retries.

1.  Added support for configuring the permissions reported to the Hadoop
    `FileSystem` layer; the permissions are still fixed per `FileSystem`
    instance and aren't actually enforced, but can now be set with:

    ```
    fs.gs.reported.permissions [default = "700"]
    ```

    This allows working around some clients like Hive-related daemons and tools
    which pre-emptively check for certain assumptions about permissions.

### 1.4.1 - 2015-07-09

1.  Switched from the custom SeekableReadableByteChannel to Java 7's
    `java.nio.channels.SeekableByteChannel`.

1.  Removed the configurable but default-constrained 250GB upload limit; uploads
    can now exceed 250GB without needing to modify config settings.

1.  Added helper classes related to GCS retries.

1.  Added workaround support for read retries on objects with content-encoding
    set to gzip; such content encoding isn't generally correct to use since it
    means filesystem reported bytes will not match actual read bytes, but for
    cases which accept byte mismatches, the read channel can now manually seek
    to where it left off on retry rather than having a `GZIPInputStream` throw
    an exception for a malformed partial stream.

1.  Added an option for enabling "direct uploads" in
    GoogleCloudStorageWriteChannel which is not directly used by the Hadoop
    layer, but can be used by clients which directly access the lower
    GoogleCloudStorage layer.

1.  Added CreateBucketOptions to the `GoogleCloudStorage` interface so that
    clients using the low-level `GoogleCloudStorage` directly can create buckets
    with different locations and storage classes.

1.  Fixed
    [issue](https://github.com/GoogleCloudDataproc/hadoop-connectors/issues/5)
    where stale cache entries caused stuck phantom directories if the
    directories were deleted using non-Hadoop-based GCS clients.

1.  Fixed a bug which prevented the Apache HTTP transport from working with
    Hadoop 2 when no proxy was set.

1.  Misc updates in library dependencies; google.api.version
    (com.google.http-client, com.google.api-client) updated from 1.19.0 to
    1.20.0, google-api-services-storage from v1-rev16-1.19.0 to v1-rev35-1.20.0,
    and google-api-services-bigquery from v2-rev171-1.19.0 to v2-rev217-1.20.0,
    and Guava from 17.0 to 18.0.

### 1.4.0 - 2015-05-27

1.  The new inferImplicitDirectories option to `GoogleCloudStorage` tells it to
    infer the existence of a directory (such as `foo`) when that directory node
    does not exist in GCS but there are GCS files that start with that path
    (such as `foo/bar`). This allows the GCS connector to be used on read-only
    filesystems where those intermediate directory nodes can not be created by
    the connector. The value of this option can be controlled by the Hadoop
    boolean config option `fs.gs.implicit.dir.infer.enable`. The default value
    is true.

1.  Increased Hadoop dependency version to 2.6.0.

1.  Fixed a bug introduced in 1.3.2 where, during marker file creation, file
    info was not properly updated between attempts. This lead to
    backoff-retry-exhaustion with `412-preconditon-not-met` errors.

1.  Added support for changing the HttpTransport implementation to use, via
    `fs.gs.http.transport.type = [APACHE | JAVA_NET]`

1.  Added support for setting a proxy of the form `host:port` via
    `fs.gs.proxy.address`, which works for both `APACHE` and `JAVA_NET`
    `HttpTransport` options.

1.  All logging converted to use `slf4j` instead of the previous
    `org.apache.commons.logging.Log`; removed the `LogUtil` wrapper which
    previously wrapped `org.apache.commons.logging.Log`.

1.  Automatic retries for premature end-of-stream errors; the previous behavior
    was to throw an unrecoverable exception in such cases.

1.  Made `close()` idempotent for `GoogleCloudStorageReadChannel`.

1.  Added a low-level method for setting Content-Type metadata in the
    `GoogleCloudStorage` interface.

1.  Increased default `DirectoryListCache` TTL to 4 hours, wired out TTL
    settings as top-level config params:

    ```
    fs.gs.metadata.cache.max.age.entry.ms
    fs.gs.metadata.cache.max.age.info.ms
    ```

### 1.3.3 - 2015-02-26

1.  When performing a retry in `GoogleCloudStorageReadChannel`, attempts to
    `close()` the underlying channel are now performed explicitly instead of
    waiting for `performLazySeek()` to do it, so that `SSLException` can be
    caught and ignored; broken SSL sockets cannot be closed normally, and are
    responsible for already cleaning up on error.

1.  Added explicit check of `currentPosition == size` when `-1` is read from
    underlying stream in `GoogleCloudStorageReadChannel`, in case the stream
    fails to identify an error case and prematurely reaches end-of-stream.

### 1.3.2 - 2015-01-22

1.  In the create file path, marker file creation is now configurable. By
    default, marker files will not be created. The default is most suitable for
    MapReduce applications. Setting fs.gs.create.marker.files.enable to true in
    `core-site.xml` will re-enable marker files. The use of marker files should
    be considered for applications that depend on early failing when two
    concurrent writes attempt to write to the same file. Note that file
    overwrites semantics are preserved with or without marker files, but
    failures will occur sooner with marker files present.

### 1.3.1 - 2014-12-16

1.  Fixed a rare `NullPointerException` in `FileSystemBackedDirectoryListCache`
    which can occur if a directory being listed is purged from the cache between
    a call to `exists()` and `listFiles()`.

1.  Fixed a bug in GoogleHadoopFileSystemCacheCleaner where cache-cleaner fails
    to clean any contents when a bucket is non-empty but expired.

1.  Fixed a bug in `FileSystemBackedDirectoryListCache` which caused garbage
    collection to require several passes for large directory hierarchies; now we
    can successfully garbage-collect an entire expired tree in a single pass,
    and cache files are also processed in-place without having to create a
    complete in-memory list.

1.  Updated handling of new file creation, file copying, and file deletion so
    that all object modification requests sent to GCS contain preconditions that
    should prevent race-conditions in the face of retried operations.

### 1.3.0 - 2014-10-17

1.  Directory timestamp updating can now be controlled via user-settable
    properties `fs.gs.parent.timestamp.update.enable`,
    `fs.gs.parent.timestamp.update.substrings.excludes`, and
    `fs.gs.parent.timestamp.update.substrings.includes` in `core-site.xml`. By
    default, timestamp updating is enabled for the YARN done and intermediate
    done directories and excluded for everything else. Strings listed in
    includes take precedence over excludes.

1.  Directory timestamp updating will now occur on a background thread inside
    GoogleCloudStorageFileSystem.

1.  Attempting to acquire an OAuth access token will be now be retried when
    using `.p12` or installed application (JWT) credentials if there is a
    recoverable error such as an HTTP `5XX` response code or an `IOException`.

1.  Added FileSystemBackedDirectoryListCache, extracting a common interface for
    it to share with the `(InMemory)DirectoryListCache`; instead of using an
    in-memory HashMap to enforce only same-process list consistency, the
    FileSystemBacked version mirrors GCS objects as empty files on a local
    FileSystem, which may itself be an NFS mount for cluster-wide or even
    potentially cross-cluster consistency groups. This allows a cluster to be
    configured with a "consistent view", making it safe to use GCS as the
    `DEFAULT_FS` for arbitrary multi-stage or even multi-platform workloads.
    This is now enabled by default for machine-wide consistency, but it is
    strongly recommended configuring clusters with an NFS directory for
    cluster-wide strong consistency. Relevant configuration settings:

    ```
    fs.gs.metadata.cache.enable [default: true]
    fs.gs.metadata.cache.type [IN_MEMORY (default) | FILESYSTEM_BACKED]
    fs.gs.metadata.cache.directory [default: /tmp/gcs_connector_metadata_cache]
    ```

1.  Optimized seeks in GoogleHadoopFSDataInputStream which fit within the
    pre-fetched memory buffer by simply repositioning the buffer in-place
    instead of delegating to the underlying channel at all.

1.  Fixed a performance-hindering bug in globStatus where `foo/bar/*` would
    flat-list `foo/bar` instead of `foo/bar/`; causing the "candidate matches"
    to include things like `foo/bar1` and `foo/bar1/baz`, even though the
    results themselves would be correct due to filtering out the proper glob
    client-side in the end.

1.  The versions of java API clients were updated to 1.19 derived versions.

### 1.2.9 - 2014-09-18

1.  When directory contents are updated e.g., files or directories are added,
    removed, or renamed the GCS connector will now attempt to update a metadata
    property on the parent directory with a modification time. The modification
    time recorded will be used as the modification time in subsequent
    `FileSystem#getStatus(...)`, `FileSystem#listStatus` and
    `FileSystem#globStatus(...)` calls and is the time as reported by the system
    clock of the system that made the modification.

### 1.2.8 - 2014-08-07

1.  Changed the manner in which the GCS connector jar is built to A) reduce
    included dependencies to only those parts which are used and B) repackaged
    dependencies whose versions conflict with those bundled with Hadoop.

1.  Deprecated `fs.gs.system.bucket` config.

### 1.2.7 - 2014-06-23

1.  Fixed a bug where certain globs incorrectly reported the parent directory
    being not found (and thus erroring out) in Hadoop 2.2.0 due to an
    interaction with the `fs.gs.glob.flatlist.enable` feature; doesn't affect
    Hadoop 1.2.1 or 2.4.0.

### 1.2.6 - 2014-06-05

1.  When running in Hadoop 0.23+ (Hadoop 2+), listStatus will now throw a
    FileNotFoundException when a non-existent path is passed in.

1.  The GCS connector now uses the v1 JSON API when accessing Google Cloud
    Storage.

1.  The GoogleHadoopFileSystem now treats the parent of the root path as if it
    is the root path. This behavior mimics the POSIX behavior of `/..` being the
    same as `/`.

1.  When creating a new file, a zero-length marker file will be created before
    the FSDataOutputStream is returned in create(). This allows for early
    detection of overwrite conflicts that may occur and prevents certain race
    conditions that could be encountered when relying on a single exists()
    check.

1.  The dependencies on cglib and asm were removed from the GCS connector and
    the classes for these are no longer included in the JAR.

### 1.2.5 - 2014-05-08

1.  Fixed a bug where `fs.gs.auth.client.file` was unconditionally being
    overwritten by a default value.

1.  Enabled direct upload for directory creation to save one round-trip call.

1.  Added wiring for `GoogleHadoopFileSystem.close()` to call through to
    `close()` its underlying helper classes as well.

1.  Added a new batch mode for creating directories in parallel which requires
    manually parallelizing in the client. Speeds up nested directory creation
    and repairing large numbers of implicit directories in listStatus.

1.  Eliminated redundant API calls in listStatus, speeding it up by ~half.

1.  Fixed a bug where globStatus didn't correctly handle globs containing `?`.

1.  Implemented new version of globStatus which initially performs a flat
    listing before performing the recursive glob logic in-memory to dramatically
    speed up globs with lots of directories; the new behavior is default, but
    can be disabled by setting `fs.gs.glob.flatlist.enable=false`.

### 1.2.4 - 2014-04-09

1.  The value of `fs.gs.io.buffersize.write` is now rounded up to 8MB if set to
    a lower value, otherwise the backend will error out on unaligned chunks.

1.  Misc refactoring to enable reuse of the resumable upload classes in other
    libraries.

### 1.2.3 - 2014-03-21

1.  Fixed a bug where renaming a directory could cause the file contents to get
    shuffled between files when the fully-qualified file paths have different
    lengths. Does not apply to rename on files directly, such as when using a
    glob expression inside a flat directory.

1.  Changed the behavior of batch request API calls such that they are retried
    on failure in the same manner as non-batch requests.

1.  Eliminated an unnecessary dependency on `com/google/protobuf` which could
    cause version-incompatibility issues with Apache Shark.

### 1.2.2 - 2014-02-12

1.  Fixed a bug where filenames with `+` were unreadable due to premature
    URL-decoding.

1.  Modified a check to allow `fs.gs.io.buffersize.write` to be a non-multiple
    of 8MB, just printing out a warning instead of check-failing.

1.  Added some debug-level logging of exceptions before throwing in cases where
    Hadoop tends to swallow the exception along with its useful info.

### 1.2.1 - 2014-01-23

1.  Added `CHANGES.txt` for release notes.

1.  Fixed a bug where accidental URI decoding make it impossible to use
    pre-escaped filenames, e.g. `foo%3Abar`. This is necessary for Pig to work.

1.  Fixed a bug where an `IOException` was thrown when trying to read a
    zero-byte file. Necessary for Pig to work.

### 1.2.0 - 2014-01-14

1.  Preview release of GCS connector.
