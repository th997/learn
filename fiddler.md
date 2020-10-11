
# 脚本支持
Fiddler菜单 >> Rules >> Customize Rules

## 保存文件
```
		if (oSession.fullUrl.Contains("https://xxx/search/xxx"))
		{
			oSession.utilDecodeResponse();//消除保存的请求可能存在乱码的情况
			var fso;
			var file;
			fso = new ActiveXObject("Scripting.FileSystemObject");
			//文件保存路径，可自定义
			file = fso.OpenTextFile("D:\\list_"+new Date().getTime(),8 ,true, true);
			file.writeLine(oSession.GetResponseBodyAsString());
			file.close();
		}
```

## 修改响应
```
static function OnBeforeResponse(oSession: Session) {
if (m_Hide304s && oSession.responseCode == 304) {
oSession["ui-hide"] = "true";
}
if (oSession.RequestBody.Length<=0) {
oSession["ui-hide"] = "true";
}
if(!oSession.url.Contains("fenqile")){
oSession["ui-hide"] = "true";
}
if(!oSession.url.Contains("sdk_collect")){
//oSession["ui-hide"] = "true";
}
// 修改响应
//oSession.utilSetResponseBody('');
}
```
