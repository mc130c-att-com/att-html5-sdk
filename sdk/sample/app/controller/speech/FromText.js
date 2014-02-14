/**
 * Controller that interacts with the Text to Speech application.
 */
Ext.define('SampleApp.controller.speech.FromText', {
	extend: 'Ext.app.Controller',

	requires: [
		'Att.Provider',
		'Att.ApiResults',
		'SampleApp.Config'
	],

	config: {
		provider: undefined,
		refs: {
			view: 'att-speech-fromtext',
			buttonSubmit: 'button[action=submitText]',
			buttonPlay: 'button[action=playConvertedSpeech]',
			text: 'textareafield[name=textToConvert]'
		},
		control: {
			'button[action=submitText]': { 'tap': 'onSubmitText' },
			'button[action=playConvertedSpeech]': { 'tap': 'onPlaySound' },
			'textareafield[name=textToConvert]': { 'change': 'onTextChange', 'keyup': 'onTextChange' }
		}
	},
	controls: {},
	onTextChange: function () {
		this.setResult("");
		this.controls.buttonPlay.setDisabled(true);
        var me = this;
        setTimeout(function(){
            me.controls.buttonSubmit.setDisabled(me.controls.text._value.length < 1);
        }, 0);
	},
	launch: function () {

		this.controls = {
			buttonSubmit: this.getButtonSubmit(),
			buttonPlay: this.getButtonPlay(),
			text: this.getText()
		};
	},
	onPlaySound: function () {
        var contextType = window.AudioContext || window.webkitAudioContext;
        var context = new contextType();
        var source = context.createBufferSource();
        source.buffer = this.audioBuffer;
        source.connect(context.destination);
        source.start();
	},
	setResult: function(text) {
		document.getElementById("resultWindow").innerHTML = text;
	},
	onSubmitText: function () {
		this.controls.buttonSubmit.setDisabled(true);
		var me = this;

		AttApiClient.textToSpeech(
			this.controls.text._value,
			function (blob) {
                var contextType = window.AudioContext || window.webkitAudioContext;
                var context = new contextType();
                var reader = new FileReader();
                reader.addEventListener("loadend", function() {
                    context.decodeAudioData(reader.result, function(buffer){ 
                        me.audioBuffer = buffer;
                        me.controls.buttonPlay.setDisabled(false);
                        me.setResult("Success, click Play to hear the converted audio");
                    })
                });
                reader.readAsArrayBuffer(blob);
			},
			function (error) {
				me.setResult(error);
			}
		);
	}
});