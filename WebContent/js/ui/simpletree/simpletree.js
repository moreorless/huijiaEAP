var SimpleTree = {

	defopts : {
		onclick: function(event) {},
		ondblclick: function(event) {},
		vertical: false
	},

	draw : function(options) {
		var opt = $.extend({}, SimpleTree.defopts, options || {});
		
		SimpleTree.prepare(opt.root);
		var $root = $(opt.root.dom), 
			nodeWidth = opt.vertical ? $root.outerWidth() + 40 : $root.outerWidth(true), 
			nodeHeight = opt.vertical ? $root.outerHeight(true) : $root.outerHeight() + 40, 
			containerWidth = $(opt.container).width(),
			containerHeight = $(opt.container).height();
		opt.width = nodeWidth;
		opt.height = nodeHeight;
		if (opt.vertical) {
			var maxTop = SimpleTree.layoutV(opt.root, nodeWidth, nodeHeight);
			var treeHeight = maxTop + nodeHeight;
			if (treeHeight < containerHeight) {
				var distance = (containerHeight - treeHeight) / 2;
				opt.root.top = opt.root.top + distance;
				if (opt.root.children && opt.root.children.length > 0)
					SimpleTree.moveBottom(opt.root.children, distance);
			}
		} else {
			var maxLeft = SimpleTree.layout(opt.root, nodeWidth, nodeHeight);
			var treeWidth = maxLeft + nodeWidth;
			if (treeWidth < containerWidth) { //当级联树较窄时，将整个树放置在container的中间位置显示
				var distance = (containerWidth - treeWidth) / 2;
				opt.root.left = opt.root.left + distance;
				if (opt.root.children && opt.root.children.length > 0)
					SimpleTree.moveRight(opt.root.children, distance);
			}
		}
		SimpleTree.drawnode(opt.root, opt.container, opt);
	},
	
	drawnode : function(node, container, options, type) {
		if (typeof type == "undefined")
			type = "root";

		var headerCss;
		if (options.vertical)
			headerCss = {
				"margin-top": node.distance > 0 && type.indexOf("left") < 0 ? -node.distance + "px" : null,
				"height": node.distance > 0 && type.indexOf("left") < 0 ? node.distance + node.height + "px" : null
			};
		else
			headerCss = {
				"margin-left": node.distance > 0 && type.indexOf("left") < 0 ? -node.distance + "px" : null,
				"width": $.browser.msie && node.distance > 0 && type.indexOf("left") < 0 ? node.distance + node.width + "px" : null
			};
		var nodewrapper = $("<div/>", {
			css: {
				top: node.top + "px",
				left: node.left + "px",
				width: options.width,
				height: options.height
			},
			id: node.dom.id + "-wrapper",
			"class": "node-wrapper " + type + ($(node.dom).hasClass("crosslevel") ? " crosslevel" : "") + (options.vertical ? " vertical" : " horizon")
		}).append($("<div/>", {
			"class": "header node-line",
			css: headerCss
		})).append(node.dom).append($("<div/>", {
			"class": "footer node-line"
		})).prependTo(container);

		// Draw children
		if ((!node.collapsed) && node.children && node.children.length > 0) { // Has Children and is Expanded
			for (var i = 0; i < node.children.length; i++) {
				var type = i == 0 ? (node.children.length == 1 ? "left single-pass" : "left") : (i == node.children.length - 1 ? "right" : "mid");
				SimpleTree.drawnode(node.children[i], container, options, type);
			}
		} else
			nodewrapper.addClass("leaf");
	},
	
	layout: function(node, nodeWidth, nodeHeight) {
		var nodeLeft = 0; // defaultValue 

		// Before Layout this Node, Layout its children
		var childLeft = 0;
		if ((!node.collapsed) && node.children && node.children.length>0) {
			for (var i = 0; i < node.children.length; i++) {
				childLeft = Math.max(childLeft, SimpleTree.layout(node.children[i], nodeWidth, nodeHeight));
			}
		}

		if ((!node.collapsed) && node.children && node.children.length > 0) { // If Has Children and Is Expanded

			// My left is in the center of my children
			var childrenWidth = (node.children[node.children.length-1].left + nodeWidth) - node.children[0].left;
			nodeLeft = (node.children[0].left + (childrenWidth / 2)) - (nodeWidth / 2);

			// Is my left over my left node?
			// Move it to the right
			if(node.leftNode&&((node.leftNode.left+nodeWidth)>nodeLeft)) {
				var newLeft = node.leftNode.left + nodeWidth;
				var diff = newLeft - nodeLeft;
				/// Move also my children
				SimpleTree.moveRight(node.children, diff);
				nodeLeft = newLeft;
			}
		} else {
			// My left is next to my left sibling
			if (node.leftNode) 
				nodeLeft = node.leftNode.left + nodeWidth;
		}
		
		if (node.leftNode)
			node.distance = nodeLeft - node.leftNode.left - nodeWidth;

		node.left = nodeLeft;

		// The top depends only on the level
		node.top = (nodeHeight * (node.level/* + 1*/));
		
		node.width = nodeWidth;
		node.height = nodeHeight;
		
		return Math.max(node.left, childLeft);
	},
	
	moveRight: function(nodes, distance) {
		for (var i = 0; i < nodes.length; i++) {
			nodes[i].left += distance;
			if (nodes[i].children) {
				SimpleTree.moveRight(nodes[i].children, distance);
			}
		}
	},
	
	layoutV: function(node, nodeWidth, nodeHeight) {
	    var nodeTop = 0; // defaultValue 

	    var childTop = 0;
	    // Before Layout this Node, Layout its children
	    if ((!node.collapsed) && node.children && node.children.length > 0) {
	        for (var i = 0; i < node.children.length; i++) {
	            childTop = Math.max(SimpleTree.layoutV(node.children[i], nodeWidth, nodeHeight), childTop);
	        }
	    }

	    if ((!node.collapsed) && node.children && node.children.length > 0) { // If Has Children and Is Expanded

	        // My Top is in the center of my children
	        var childrenHeight = (node.children[node.children.length - 1].top + nodeHeight) - node.children[0].top;
	        nodeTop = (node.children[0].top + (childrenHeight / 2)) - (nodeHeight / 2);

	        // Is my top over my previous sibling?
	        // Move it to the bottom
	        if (node.leftNode && ((node.leftNode.top + nodeHeight) > nodeTop)) {
	            var newTop = node.leftNode.top + nodeHeight;
	            var diff = newTop - nodeTop;
	            /// Move also my children
	            SimpleTree.moveBottom(node.children, diff);
	            nodeTop = newTop;
	        }

	    } else {
	        // My top is next to my top sibling
	        if (node.leftNode)
	            nodeTop = node.leftNode.top + nodeHeight;
	    }

		if (node.leftNode)
			node.distance = nodeTop - node.leftNode.top - nodeHeight;

	    node.top = nodeTop;

	    // The Left depends only on the level
	    node.left = (nodeWidth * (node.level/* + 1*/));
	    // Size is constant
	    node.height = nodeHeight;
	    node.width = nodeWidth;

	    return Math.max(node.top, childTop);
	},
	
	moveBottom: function(nodes, distance) {
	    for (var i = 0; i < nodes.length; i++) {
	        nodes[i].top += distance;
	        if (nodes[i].children) {
	            SimpleTree.moveBottom(nodes[i].children, distance);
	        }   
	    }
	},
	
	prepare : function(node, level, parentNode, leftNode, rightLimits) {

		if (level == undefined) level = 0;
		if (parentNode == undefined) parentNode = null;
		if (leftNode == undefined) leftNode = null;
		if (rightLimits == undefined) rightLimits = [];

		node.level = level;
		node.parentNode = parentNode;
		node.leftNode = leftNode;
		node.top = node.left = node.distance = 0;

		if ((!node.collapsed) && node.children && node.children.length > 0) { // Has children and is expanded
			for (var i = 0; i < node.children.length; i++) {
				var left = null;
				if (i == 0 && rightLimits[level]!=undefined) left = rightLimits[level];
				if (i > 0) left = node.children[i - 1];
				if (i == (node.children.length-1)) rightLimits[level] = node.children[i];
				SimpleTree.prepare(node.children[i], level + 1, node, left, rightLimits);
			}
		}
	}
};
