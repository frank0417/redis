<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'china.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->


<script type="text/javascript">
function createXMLHttpRequest() {
	try {
		return new XMLHttpRequest();//大多数浏览器
	} catch (e) {
		try {
			return ActvieXObject("Msxml2.XMLHTTP");//IE6.0
		} catch (e) {
			try {
				return ActvieXObject("Microsoft.XMLHTTP");//IE5.5及更早版本	
			} catch (e) {
				alert("哥们儿，您用的是什么浏览器啊？");
				throw e;
			}
		}
	}
}
/*
 * 1. 在文档加载完毕时发送请求，得到所有省份名称，显示在<select name="province"/>中
 */

window.onload = function() {
	/*
	ajax四步，请求RegionServlet，得到所有省份名称
	使用每个省份名称创建一个<option>元素，添加到<select name="province">中
	*/
	
	var xmlHttp = createXMLHttpRequest();
	xmlHttp.open("get", "<c:url value='/RegionServlet'/>", true);
	xmlHttp.send(null);
	xmlHttp.onreadystatechange = function() {
		if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			// 获取服务器的响应
			var text = xmlHttp.responseText;
			// 使用逗号分隔它，得到数组
			var arr = text.split(",");
			// 循环遍历每个省份名称，每个名称生成一个option对象，添加到<select>中
			for(var i = 0; i < arr.length; i++) {
				var op = document.createElement("option");//创建一个指名名称元素
				op.value = arr[i];//设置op的实际值为当前的省份名称
				var textNode = document.createTextNode(arr[i]);//创建文本节点
				op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值
				
				document.getElementById("p").appendChild(op);
			}
		}
	};
	
	
	/*
	第二件事情：给<select name="province">添加改变监听
	使用选择的省份名称请求CityServlet，得到<province>元素
	获取<province>元素中所有的<city>元素，遍历之！获取每个<city>的文本内容，即市名称
	使用每个市名称创建<option>元素添加到<select name="city">
	*/
	var proSelect = document.getElementById("p");
	proSelect.onchange = function() {
		var xmlHttp = createXMLHttpRequest();
		xmlHttp.open("POST", "<c:url value='/CityServlet'/>", true);
		xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		//requet.setAttribute("province", proSelect.value);
		xmlHttp.send("province=" + proSelect.value);//把下拉列表中选择的值发送给服务器！
		xmlHttp.onreadystatechange = function() {
		if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			// 获取服务器的响应
			var text = xmlHttp.responseText;
			// 使用逗号分隔它，得到数组
			var arr = text.split(",");
			
			//清空之前的option内的值
			document.getElementById("c").options.length=0;
			// 循环遍历每个城市名称，每个名称生成一个option对象，添加到<select>中
			for(var i = 0; i < arr.length; i++) {
				var op = document.createElement("option");//创建一个指名名称元素
				op.value = arr[i];//设置op的实际值为当前的省份名称
				var textNode = document.createTextNode(arr[i]);//创建文本节点
				op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值
				
				document.getElementById("c").appendChild(op);
			}
		}
	};
	};
};
</script>


  </head>
  
  <body>
     <h1>省市联动</h1>
    
     <select name="province" id="p">
         <option value>请选择省</option>>
     </select>
    
     <select name="city" id="c">
         <option>请选择市</option>
     </select>
    
  </body>
</html>
