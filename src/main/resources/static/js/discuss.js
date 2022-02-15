function like(obj,entityType,entityId,entityUserId,postId) {
    $.post(
        PROJECT_ROOT + "/like/giveLike",
        {"entityType":entityType,"entityId":entityId,"entityUserId":entityUserId,"postId":postId},
        function (data) {
            data = $.parseJSON(data);
            if(data.code==0){
                $(obj).children("i").text(data.likeCount);
                $(obj).children("b").text(data.likeStatus==1?'已赞':'赞');
            }else{
                alert(data.msg);
            }
        }
    )
}