var fixHelper = function(e, ui) {   
	ui.children().each(function() {  
		$(this).width($(this).width());  //在拖动时，拖动行的cell（单元格）宽度会发生改变。在这里做了处理就没问题了   
	});  
	return ui;  
};

function changeSort(url,sortable,filterChar,firstFlag, secondFlag){
	$(filterChar).css('cursor','move');
	$("."+ sortable).sortable({
	    cursor: 'move',
	    items: filterChar,
	    opacity: 0, //拖动时，透明度为0.6
	    revert: true, //释放时，增加动画 
	    helper: fixHelper,                  //调用fixHelper
	    zIndex: 9999,
	    axis :'y',
	    start:function(e, ui){
	        ui.helper.css({"background":"#fff"});     //拖动时的行，要用ui.helper
	        return ui;
	    },
	    stop : function (event, ui){ 
	    	//这里可以写任何‘移动结束’时触发的函数；
	    	$( "."+ sortable ).sortable( "refresh" );
	    },
	    update: function(event, ui) { 	
		    //更新排序之后
		    var ids = [];
		    var sorts = [];
		    var IdsAndSorts = $(this).sortable("toArray").toString().replace(firstFlag,"").split(',');
		    $.each(IdsAndSorts,function(i,val){
		    	ids.push(val.split(secondFlag)[0]);
		    	sorts.push(val.split(secondFlag)[1]);
		    });
		    $.ajax({
		        url: url,
		        type: 'POST',
		        data: {ids:ids.join(','), sorts:sorts.join(',')},
		        success: function(json) {
		        	
		        }
		    });
	    }
	  });
	  $("."+ sortable).disableSelection();		
}