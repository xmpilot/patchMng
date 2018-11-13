
function paserFiles(){
    $.ajax({
        url:"main/parserFile",
        type:"post",
        dataType:"json",
        data:{
            srcPath: $('#srcPath').val(),
            descPath: $('#descPath').val(),
            srcFileList: $('#srcFileList').val()
        },
        beforeSend:function(){
            //
        },
        success:function(result){
            if (result.code == 0){
                var fileItem = "";
                for (var i = 0; i < result.data.parserFileList.length; i++){
                    fileItem += result.data.parserFileList[i] + "\n";
                }
                $('#patchFileList').html(fileItem);
                return
            }
            addLogs([result.message]);
        },
        complete :function(){
        }
    });
}

function clearList(){
    $('#srcFileList').html('');
    $('#patchFileList').html('');
    $('#logMsg').html('');
}

function clearDescPath(){
    $.ajax({
        url:"main/clearDescPath",
        type:"post",
        dataType:"json",
        data:{
            descPath: $('#descPath').val()
        },
        beforeSend:function(){
            //
        },
        success:function(result){
            if (result.code == 0){
                addLogs(result.data.logs);
                addLogs(['操作成功!']);
                return
            }
            addLogs([result.message]);
        },
        complete :function(){
        }
    });
}

function copyPatchFiles(){
    $.ajax({
        url:"main/copyPatchFiles",
        type:"post",
        dataType:"json",
        data:{
            srcPath: $('#srcPath').val(),
            patchFileList: $('#patchFileList').val(),
            descPath: $('#descPath').val()
        },
        beforeSend:function(){
            //
        },
        success:function(result){
            if (result.code == 0){
                addLogs(result.data.logs);
                addLogs(['操作成功!']);
                return
            }
            addLogs([result.message]);
        },
        complete :function(){
        }
    });
}

function addLogs(logs){
    if (logs == null){
        return;
    }
    for (var i = 0; i < logs.length; i++){
        $('#logMsg').append(logs[i] + "\n");
    }
}
