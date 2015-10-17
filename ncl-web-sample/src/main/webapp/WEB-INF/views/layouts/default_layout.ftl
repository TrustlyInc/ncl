<#escape x as x?html>
<!DOCTYPE html>
<head>
	<title>PayWithMyBank - <@yield to="title"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="${base_url}/assets/public/styles/panel-v2.css" type="text/css" media="all" />
	<@yield to="head"/>
</head>
<body class="initializing ${body_classes!}<#if (flow.body_classes)??> ${flow.body_classes!}</#if>" data-route="${route!}" data-panel="payment" data-layout="lightbox2">
	<#noescape>${page_content}</#noescape>
</body>
</html>
</#escape>