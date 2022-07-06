# ec-oms-springcloud
<p>这是一个SpringCloudAlibaba组建搭建并有gradle进行构建微服务项目</p>

### ec-oms-gateway

<ol>
<li>该服务是所有请求的入口，通过该服务请求将被转发到其它的server上并在处理完后返回resp</li>
<li>该服务定义了全局过滤器和局部过滤器，并将配置和应用信息注册到nacos上</li>
</ol>

