
$(document).ready(function() {
    $.ajax({
        url: "https://blox.weareblox.com/api/markets"  
    }).then(function(data) {
       $('.coinid').append(data[0].name);
       $('.pricepoint').append(data[0].price.amount);
$('.sellprice').append(data[0].sellPrice.amount);
    });
});

