# starter-swagger
swagger集成包

# 使用步骤
1、在配置中加入配置：swagger.doc.prefixs=/smsTemplate,/publicNotice （需要公布或者调试的接口url前缀，逗号隔开）

2、启动服务后访问url:  http://localhost:5380/swagger-ui.html  （IP和端口即你服务部署的IP和端口）即可。


注意：	
1、需要规范接口url定义，否则swagger.doc.prefixs配置比较麻烦。

2、必须在@RequestMapping注明method是POST还是Get，否则SwaggerUI中将生成该接口的所有方法（包括PUT,DELETE,POST等）
