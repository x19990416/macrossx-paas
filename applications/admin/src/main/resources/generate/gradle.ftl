plugins {
  id 'java'
  id 'application'
}
repositories {
  jcenter()
}

dependencies {
    <#list dependencies as independency>
        implements ${independency}
    </#list>



// Use JUnit test framework
}

<#if mainClassName??>
    mainClassName = '${mainClassName}'
</#if>

