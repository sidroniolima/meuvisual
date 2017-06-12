/**
 * Pega os valores do slider e chama a pesquisa por valores.
 * 
 */

function getSliderValues(sliderValue)
{
	urlPesquisaPorValor({ min : sliderValue.value[0], max : sliderValue.value[1]});
}