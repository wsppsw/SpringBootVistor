$(function (){


    var map = new BMap.Map("allmap");
    var address = $("#address").val();

    var zuobiao = $("#zuobiao").val();
    var cccc= $("#scity").val();
    var coor=zuobiao.split(",");
    let left1=coor[0];
    let right1=coor[1];
    var address_old=address;
    console.log(address_old);
    var address_new;
    let count=0;
    var t;
    t=setInterval(function (){
        address = $("#address").val();
        address_new =address;
        console.log(address_new);

        zuobiao = $("#zuobiao").val();
        cccc= $("#scity").val();
        coor=zuobiao.split(",");
        left1=coor[0];
        right1=coor[1];
        count++;


        var point = new BMap.Point(left1,right1);
        map.centerAndZoom(point, 12);
        map.enableScrollWheelZoom();

        var myIcon = new BMap.Icon("../img/ico/z.ico",new BMap.Size(30,30),{
            anchor: new BMap.Size(10,10)
        });

        var marker=new BMap.Marker(point,{icon: myIcon});
        map.addOverlay(marker);

        $("#path").click(function(){
            path();
        });

        map.addOverlay(marker);
        var licontent="<b>目标景点</b><br>";
        licontent+="<span><strong>地址：</strong>"+address+"</span><br>";
        var opts = {
            width : 200,
            height: 80,
        };
        var  infoWindow = new BMap.InfoWindow(licontent, opts);
        marker.openInfoWindow(infoWindow);
        marker.addEventListener('click',function(){
            marker.openInfoWindow(infoWindow);
        });

        function path(){
            var geolocation = new BMap.Geolocation();
            geolocation.getCurrentPosition(function(r){
                if(this.getStatus() == BMAP_STATUS_SUCCESS){
                    var mk = new BMap.Marker(r.point);
                    map.addOverlay(mk);
                    //map.panTo(r.point);//地图中心点移到当前位置
                    var latCurrent = r.point.lat;
                    var lngCurrent = r.point.lng;

                    window.open("http://api.map.baidu.com/direction?origin="+latCurrent+","+lngCurrent+"&destination="+right1+","+left1+"&mode=driving&region="+cccc+"&output=html");
                    //location.href="http://api.map.baidu.com/direction?origin="+latCurrent+","+lngCurrent+"&destination=36.938712,113.881628&mode=driving&region=石家庄&output=html";

                }
                else {
                    alert('failed'+this.getStatus());
                }
            },{enableHighAccuracy: true})

        }

        if(count===1){
            clearInterval(t);
        }
    },100);



})