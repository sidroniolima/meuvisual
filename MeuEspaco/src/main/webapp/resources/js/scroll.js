/**
 * Scroll do body para a área de visualização da mensagem.
 * @returns
 */
function scrollMessages() 
{
    var posicaoBreadcrumb = $('.breadcrumb').offset().top;

    $('html,body').animate(
    {
        scrollTop: posicaoBreadcrumb + "px"
    }, 1000);
}	