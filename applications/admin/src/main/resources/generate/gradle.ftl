plugins {
  id 'java'
  id 'application'
}
repositories {
  maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
  maven { url 'https://oss.sonatype.org/content/repositories/snapshots'}
  jcenter()
}

dependencies {
<#list dependencies as independency>
    ${independency}
</#list>
}

<#if mainClassName??>
    mainClassName = '${mainClassName}'
</#if>

