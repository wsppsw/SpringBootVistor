$(function(){
	$("#div2").show();
	$("#div4").hide();
	$("#li1").hover(function(){
		$("#div2").show();
		$("#div4").hide();
	},function(){
		
	})

	$("#li2").hover(function(){
		$("#div4").show();
		$("#div2").hide();
	},function(){
		
	})
})