$(document).ready(function () {

    $(".btn-num-product-up").click(function () {
        var cid = $(this).closest("tr").attr("data-id");
        var qty = parseInt($(this).closest("tr").children().children().children(".qty-item").val());
        var totalItem = $(this).closest("tr").children(".total-item");
        $.get({
            url: `/update-qty/${cid}/${qty}`,
            dataType: "json",
            success: function (data) {
                totalItem.text('$' + data.sumItem.toFixed(2));
                $(".sub-total").html('$' + data.subTotal.toFixed(2));
            }
        })
    });

    $(".btn-num-product-down").click(function () {
        var cid = $(this).closest("tr").attr("data-id");
        var qty = parseInt($(this).closest("tr").children().children().children(".qty-item").val());
        var totalItem = $(this).closest("tr").children(".total-item");
        $.get({
            url: `/update-qty/${cid}/${qty}`,
            dataType: "json",
            success: function (data) {
                totalItem.text('$' + data.sumItem.toFixed(2));
                $(".sub-total").html('$' + data.subTotal.toFixed(2));
            }
        })
    });


    $(".rmv-product").click(function () {
        var cartId = $(this).closest("tr").attr("data-id");
        $.get({
            url: `/remove-cart-item/${cartId}`,
            dataType: "json",
            success: function (data) {
                if (data.size == 0) {
                    window.location.replace("http://localhost:9999/shopping-cart");
                }
                $('.count-cart').attr("data-notify", data.size);
                $('.sub-total').html('$' + data.total.toFixed(2));
            }
        })
        $(this).closest("tr").remove();
    });
})