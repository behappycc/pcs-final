/*
$(document).ready(function() {
      $('#buttonSignIn').click(function (event) {

                var valueForInput1 = $("#username").val();
                var valueForInput2 = $("#password").val();

                var data =
                {
                    key1: valueForInput1,
                    key2: valueForInput2
                };

                var dataToSend = JSON.stringify(data);

                $.ajax(
                        {
                            url: '/login',
                            type: 'POST',
                            data: dataToSend,
                            
                            success: function (jsonResponse) {
                                var objresponse = JSON.parse(jsonResponse);
                                console.log(objresponse['newkey']);

                                $("#responseLogin").text(objresponse['newkey']);

                            },
                            error: function () {
                                $("#responseLogin").text("Error to load api");

                            }
                            
                        });

                event.preventDefault();
            });
});
*/