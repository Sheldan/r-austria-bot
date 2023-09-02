allow_k8s_contexts('k8s-cluster')

load('ext://restart_process', 'docker_build_with_restart')
registry = 'harbor.sheldan.dev/sissi/'

local_resource(
  'sissi-java-compile',
  ' mvn install && ' +
  ' rm -rf application/executable/target/jar-staging && ' +
  ' unzip -o application/executable/target/sissi-exec.jar -d application/executable/target/jar-staging && ' +
  ' rsync --delete --inplace --checksum --exclude="*-SNAPSHOT.jar" -r application/executable/target/jar-staging/ application/executable/target/jar && ' +
  ' rm -rf application/executable/target/jar/snapshots && ' +
  ' mkdir application/executable/target/jar/snapshots && ' +
  ' rsync --delete --inplace --checksum --include="*/" --include="*-SNAPSHOT.jar" --exclude="*" -r application/executable/target/jar-staging/BOOT-INF/lib/ application/executable/target/jar/snapshots',
  deps=['pom.xml'])

docker_build_with_restart(
  registry + 'sissi-bot',
  './application/executable/target/jar',
  entrypoint=['java', '-noverify', '-cp', '.:./lib/*', 'dev.sheldan.sissi.executable.Application'],
  dockerfile='./application/executable/Dockerfile',
  live_update=[
    sync('./application/executable/target/jar/BOOT-INF/lib', '/app/lib'),
    sync('./application/executable/target/jar/META-INF', '/app/META-INF'),
    sync('./application/executable/target/jar/BOOT-INF/classes', '/app'),
    sync('./application/executable/target/jar/snapshots', '/app/lib')
  ],
)

docker_build(registry + 'sissi-db-data', 'deployment/image-packaging/src/main/docker/db-data/')
docker_build(registry + 'sissi-template-data', 'deployment/image-packaging/src/main/docker/template-data/')


k8s_yaml(helm('deployment/helm/sissi', values=
['./../Sissi-environments/argocd/apps/sissi/values/local/values.yaml',
'secrets://./../Sissi-environments/argocd/apps/sissi/values/local/values.secrets.yaml']
))