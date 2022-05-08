$(function(){

    var map = new BMap.Map("allmap");
    var point = new BMap.Point(113.881628,36.938712);
    map.centerAndZoom(point, 12);
    map.enableScrollWheelZoom();

    var myIcon = new BMap.Icon("../img/ico/h.ico",new BMap.Size(30,30),{
        anchor: new BMap.Size(10,10)
    });

    var marker=new BMap.Marker(point,{icon: myIcon});
    map.addOverlay(marker);


    map.addOverlay(marker);
    var licontent="<b>目标景点</b><br>";
    licontent+="<span><strong>地址：</strong>邯郸市武安市七不沟</span><br>";
    licontent+="<span><strong>电话：</strong>(010)81556565 / 6969</span><br>";
    var opts = {
        width : 200,
        height: 80,
    };
    var  infoWindow = new BMap.InfoWindow(licontent, opts);
    marker.openInfoWindow(infoWindow);
    marker.addEventListener('click',function(){
        marker.openInfoWindow(infoWindow);
    });

    $("#path").click(function(){
        alert("!!");
        path();

    })

    function path(){
        var geolocation = new BMap.Geolocation();
        geolocation.getCurrentPosition(function(r){
            if(this.getStatus() == BMAP_STATUS_SUCCESS){
                var mk = new BMap.Marker(r.point);
                map.addOverlay(mk);
                //map.panTo(r.point);//地图中心点移到当前位置
                var latCurrent = r.point.lat;
                var lngCurrent = r.point.lng;
                // alert('我的位置：'+ latCurrent + ',' + lngCurrent);
                // 36.60930793,114.48269393
                // 114.530934,37.988984
                // 114.490265,38.016287
                //114.28346,38.094277
                //113.881628,36.938712 七步沟 113.881628,36.938712
                window.open("http://api.map.baidu.com/direction?origin="+latCurrent+","+lngCurrent+"&destination=36.938712,113.881628&mode=driving&region=武安市&output=html");
                //location.href="http://api.map.baidu.com/direction?origin="+latCurrent+","+lngCurrent+"&destination=36.938712,113.881628&mode=driving&region=石家庄&output=html";

            }
            else {
                alert('failed'+this.getStatus());
            }
        },{enableHighAccuracy: true})

    }
})