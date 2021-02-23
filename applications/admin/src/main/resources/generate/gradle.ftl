plugins {
  id 'java'
  id 'application'
}
repositories {
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

