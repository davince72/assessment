/*$(document).ready(function() {
    $.ajax({
        url: "http://127.0.0.1:8080/greetings.json" 
    }).then(function(data) {
       $('.coinid').append(data.id);
       $('pricepoint').append(data.time.updated);
    });
});*/

/*$(document).ready(function() {
    $.ajax({
        url: "https://api.coindesk.com/v1/bpi/currentprice.json"  
    }).then(function(data) {
       $('.coinid').append(data.disclaimer);
       $('pricepoint').append(data.time.updated);
    });
});*/

$(document).ready(function() {
    $.ajax({
        url: "https://blox.weareblox.com/api/markets"  
    }).then(function(data) {
       $('.coinid').append(data[0].name);
       $('.pricepoint').append(data[0].price.amount);
$('.sellprice').append(data[0].sellPrice.amount);
    });
});


/*$(document).ready(function() {
    $.ajax({
        url: "http://127.0.0.1:8080/coin.json"  
    }).then(function(data) {
       $('.coinid').append(data.disclaimer);
       $('pricepoint').append(data.time.updated);
    });
});*/