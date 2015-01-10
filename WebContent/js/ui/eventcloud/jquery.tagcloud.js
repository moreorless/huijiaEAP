;(function($) {
	// private variables 私有的变量
	var oSettings;
	var oUlCss = {};//ul的样式
	var oLiCss = {};//li的样式
	var setposition = 0;//设置每个小圆IP的位置
	var topUl = -200;//设置每个小圆的纵坐标
	var leftUl = 0;//设置每个小圆的横坐标
	var fGAng = 2.399963; // sphere angle in radians*pi  2.399963
	var iXps = 0;//设置小圆每个IP的横坐标
	var iYps = 0;//设置小圆每个IP的纵坐标
	var fRad = 0;//用于计算小圆每个IP的圆周率同时用于控制每个IP的透明度
	var cloudPosition = 1;	//设置每个小园的位置
	var alphasize = 1;//设置透明度
	// default settings 默认的设置的过程的词的
	$.tagcloud = {
		 id: "TagCloud"  
		,version: "0.5.0" 
		,defaults: {			//这是默认的设置的过程
			 height: null
			,type: "cloud"		// "cloud", "list" or "sphere" 即：有三种选择的方式 云  列表  球形 
			,sizemax: 12
			,sizemin: 5
			,colormax: "048eef" //"04dcef"	// 00F
			,colormin: "003c41"  // B4D2FF
			,seed: null			// only for type=="cloud" 只适用 类型是  云 状
			,power: .4			// only for type=="sphere" 只适用 类型是  球形 的属性 原来的数值为  .5 目前以 .4这样去计算的过程
			,ullength:0			// 改动的地方获取云标签的个数 默认的值为 用于圆的摆放的位置
			,top_x:300	//以下几个参数分别调试圆的位置和圆的大小个人写的算法
			,left_y:200
			,top_a:250
			,top_b:150
			,quantitymax:120
		}
	};
	$.fn.extend({ 
		tagcloud: function(_settings) {
			oSettings = $.extend({}, $.tagcloud.defaults, _settings); //这是保留原来的对象以这种方式去传值 "{}"
			if (oSettings.seed===null) oSettings.seed = Math.ceil(Math.random()*45309714203);

			switch (oSettings.type) {
				case "sphere":
				case "cloud":
					oUlCss = {position:"absolute"};
					oLiCss = {position:"absolute",display:"block"};
				break;
				case "list":
					oUlCss = {height:"auto"};
					oLiCss = {position:"static",display:"inline"}; 
				break;
			}

			Rng.setSeed(oSettings.seed+123456);

			return this.each(function(i,o) {//这是核心的方法
				var mUl = $(o);
				var aLi = mUl.find(">li");
				var iNumLi = aLi.length;
				
				var iUlW = 400;//源码获取ul的宽度的过程 var iUlW = mUl.width();
				var iUlH = 400;//源码获取高度的算法var iUlH = oSettings.height===null?(.004*iUlW*iNumLi):oSettings.height;
				/***设置UL的位置的过程个人写的算法可以提意见对其进行优化====start****/
				leftUl = oSettings.top_a*Math.cos(2*Math.PI*cloudPosition/oSettings.ullength)+ oSettings.top_x;
				topUl = oSettings.top_b*Math.sin(2*Math.PI*cloudPosition/oSettings.ullength)+ oSettings.left_y;
				cloudPosition++;//对云标签位置确定自己写的变量
				mUl.css({width:iUlW,height:iUlH,listStyle:"none",margin:0,padding:0,top:topUl,left:leftUl});//向 ul 中添加样式的过程
				mUl.css(oUlCss);//向 ul 中添加样式的过程
				/***设置UL的位置的过程个人写的算法可以提意见对其进行优化====end****/

				var iValMx = -2147483647;//最大值固定的值
				var iValMn = 2147483648;//最小值固定的值
				var iLastVal = -1;
				for (var j=0;j<iNumLi;j++) {//遍历 li 的过程的词的
					var mLi = $(aLi[j]);//获取 li 元素 然后转换为 jquery 对象
					var iVal = parseInt(mLi.attr("data-quantity")==-1?iLastVal++:mLi.attr("data-quantity"));
					if (iVal > oSettings.quantitymax) iVal = oSettings.quantitymax;
					if (iVal>iValMx) iValMx = iVal;//最大值的判断
					if (iVal<iValMn) iValMn = iVal;//最小值的判断
					iLastVal = iVal;
				}
				var iValRn = iValMx-iValMn;

				// place on line to create minimal overlays
				var aLine = new Array();
				for (var j=0;j<iNumLi;j++) aLine[j] = j;
				//window.alert(aLine);
				for (var j, x, k = aLine.length; k; j = parseInt(Rng.rand(0,1000)/1000 * k), x = aLine[--k], aLine[k] = aLine[j], aLine[j] = x);

				iLastVal = -1;
				for (var j=0;j<iNumLi;j++) {
					var mLi = $(aLi[j]);
					/*=================对value值的控制============================*/
					var iVal = parseInt(mLi.attr("data-quantity")==-1?iLastVal++:mLi.attr("data-quantity"));
					if(iVal > oSettings.quantitymax) iVal = oSettings.quantitymax;
					iLastVal = iVal;
					//
					var fPrt = ((iNumLi-j-1)/(iNumLi-1));//?????????????
					var fPrt = (iVal-iValMn)/iValRn;//???????????????
					//
					var iSzFnt = oSettings.sizemin + fPrt*(oSettings.sizemax-oSettings.sizemin);//设置字体的大小
					//设置颜色的过程
					/*
					 * 源码所有的IP颜色通用这个算法colorRng(oSettings.colormin,oSettings.colormax,fPrt)
					 * 
					*/
					var colortag = mLi.attr("data-nodetype");
					var sColor = ""
					//colorid=0的颜色IP写死为#b00606颜色colorid=1是通过原来算法去实现
					if(colortag == "0" || colortag == "src")
					{
						sColor = "FF3300";//"b00606";
					}else if(colortag == "1" || colortag == "dest")
					{
						sColor = colorRng(oSettings.colormin,oSettings.colormax,fPrt);//设置颜色的过程
					}
					/***个人添加的一个透明度的算法====start****/
					//设置 LI 的透明度的过程先留着这个算法
//					var p = lengthsize * Math.PI * 2 / iNumLi;
//					lengthsize--;
//					var size = Math.round(10 - Math.sin(p) * 10)*10;

					alphasize = fPrt + .3;
					if (alphasize >= 1)
						alphasize = 0.95;
					
					//对透明度进行分等级的过程
//					if(iVal > 100) alphasize = 1;
//					else if(iVal >80 && iVal <= 100) alphasize = .8;
//					else if(iVal <= 80 && iVal > 50) alphasize = .7;
//					else if(iVal <= 50 && iVal > 20) alphasize = .4;
//					else  alphasize = .1;
					
					mLi.css({"fontSize":iSzFnt,position:"absolute",color:"#"+sColor,margin:0,padding:0,opacity:alphasize}).children().css({color:"#"+sColor});
					var iLiW = mLi.width();//获取 li 的宽度 宽度不断增加的过程
					var iLiH = mLi.height();//获取 li 的高度高度是不变
					
					var oCss = {};
					if (oSettings.type!="list") {
						if (oSettings.type=="cloud") {
							var iXps = Rng.rand(0,iUlW-iLiW);
							var iYps = aLine[j]*(iUlH/iNumLi) - iLiH/2;
						} else {
							var fRds = Math.pow(j/iNumLi,oSettings.power);//取次幂的过程
							fRad = (j+Math.PI/2)*fGAng;
							var setRad = .3;//这是十个事件类型的情况
							if(oSettings.ullength > 10 && oSettings.ullength < 15) setRad = .26;
							else if(oSettings.ullength >= 15 && oSettings.ullength <= 20 ) setRad = .23; 
							else if(oSettings.ullength > 20 ) setRad = .2; 
							/*
							 * 原来的算法
							 * var iXps = iUlW/2 - iLiW/2 + .5*iUlW*fRds*Math.sin(fRad);
							 * var iYps = iUlH/2 - iLiH/2 + .5*iUlH*fRds*Math.cos(fRad);
							*/
							iXps = iUlW/2 - iLiW/2 + setRad*iUlW*fRds*Math.sin(fRad)-55 + setposition;//利用的是圆周率的计算方式改过的变量
							iYps = iUlH/2 - iLiH/2 + setRad*iUlH*fRds*Math.cos(fRad)-55 + setposition;//利用的是圆周率的计算方式改过的变量
						}
						//为每个LI设置位置的
						oCss.left = iXps;
						oCss.top  = iYps;
					}
					for (var prop in oLiCss)
					{
						oCss[prop] = oLiCss[prop];
					} 
					mLi.css(oCss);   
				}
				//对小圆的大小划分等级自己添加
				var temp = 300;
				if(oSettings.ullength > 10 && oSettings.ullength < 15) temp = 280;
				else if(oSettings.ullength >= 15 && oSettings.ullength <= 20 ) temp = 260;
				else if(oSettings.ullength > 20 ) temp = 240;
				mUl.css({width:temp,height:temp});//重新的去设置UL的大小的过程
			});
		}
	});
	
	// Park-Miller RNG
	var Rng = new function() { //这是什么意思呢？？？？？random number generator :随机数发生器
		this.seed = 23145678901;
		this.A = 48271;
		this.M = 2147483647;
		this.Q = this.M/this.A;
		this.R = this.M%this.A;
		this.oneOverM = 1.0/this.M;
	}
	Rng.setSeed = function(seed) {
		this.seed = seed;
	}
	Rng.next = function() {
		var hi   = this.seed/this.Q;
		var lo   = this.seed%this.Q;
		var test = this.A*lo - this.R*hi;
		this.seed = test + (test>0?0:this.M);
		return (this.seed*this.oneOverM);
	}
	Rng.rand = function(lrn, urn) {
		return Math.floor((urn - lrn + 1) * this.next() + lrn);
	}
	// hex dec 十六进制 和 十进制的转换
	function d2h(d) {return d.toString(16);}//十进制数转换成十六进制数
	function h2d(h) {return parseInt(h,16);}//十六进制数转换成十进制数
	function getRGB(s) {
		var b3 = s.length==3;
		var aClr = [];
		for (var i=0;i<3;i++) {
			var sClr = s.substring( i*(b3?1:2), (i+1)*(b3?1:2) );
			aClr.push(h2d(b3?sClr+sClr:sClr));
		}
		return aClr;
	}
	function getHex(a) {
		var s = "";
		for (var i=0;i<3;i++) {
			var c = d2h(a[i]);
			if (c.length==1) c = "0"+c; // todo: this can be better
			s += c;
		}
		return s;
	}
	function colorRng(mn,mx,prt) {
		var aMin = getRGB(mn);
		var aMax = getRGB(mx);
		var aRtr = [];
		for (var i=0;i<3;i++) aRtr.push( aMin[i] + Math.floor(prt*(aMax[i]-aMin[i])));
		return getHex(aRtr);
	}
	// trace
	function trace(o) {
		if (window.console&&window.console.log) {
			if (typeof(o)=="string") window.console.log(o);
			else for (var prop in o) window.console.log(prop+": "+o[prop]);
		}
	};
	// set functions 意思的可以提供外部的方法的调用的过程
	$.fn.TagCloud = $.fn.Tagcloud = $.fn.tagcloud;
})(jQuery)