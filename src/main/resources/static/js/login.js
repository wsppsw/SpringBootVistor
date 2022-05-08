new Vue({
    el:"#div",
    data:{
        ruleForm:{
            username:"",
            password:"",
        },
        ruleForm1:{
            userphone:"",
            password:"",
        },
        urlimg:"img/login.gif",
        code:"点击获取验证码",
        disabled:false,
    },
    methods:{
        submitForm:function(ruleForm){

            this.$refs[ruleForm].validate((valid) => {
                if (valid) {
                    alert(this.ruleForm.username+"---"+this.ruleForm.password);
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        submitForm1:function(ruleForm1){

            this.$refs[ruleForm1].validate((valid) => {
                if (valid) {
                    alert(this.ruleForm1.userphone+"---"+this.ruleForm1.password);
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        sendPhone:function(){
            alert(this.ruleForm1.userphone);
            let seconds = 60;
            var that = this;
            that.disabled=true;
            that.code= "已经发送("+seconds+"s)";
            var time=setInterval(function(){
                seconds =seconds - 1;
                that.disabled=true;
                that.code= "已经发送("+seconds+"s)";
                let i =seconds;
                if(i<0){
                    i=1;
                    clearInterval(time);
                    that.disabled=false;
                    that.code="点击获取验证码";

                }
            },1000);
        },
        regist:function(){
            window.location.href="vue_regist.html";
        }
    }
});