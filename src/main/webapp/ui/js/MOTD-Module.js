/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

"use strict";

// Angular Module Init
const motdModule = angular.module('motdplug', ['ui.bootstrap'])

{ // Create a scope to encapsulete the helper classes and functions
	const ACTIVE_MESSAGE_NAME = "___active_message___";

	class Message {
		constructor(name, body, footer) {
			this.name   = name;
			this.body   = body;
			this.footer = footer;

			this.active = false;
		}
	}

	function isElementInViewport (el) {

		// Special bonus for those using jQuery
		if (typeof jQuery === "function" && el instanceof jQuery) {
			el = el[0];
		}

		let rect = el.getBoundingClientRect();

		return (
			rect.top >= 0 &&
			rect.left >= 0 &&
			rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && /* or $(window).height() */
			rect.right <= (window.innerWidth || document.documentElement.clientWidth) /* or $(window).width() */
		);
	}

	function fadeIn(identitifer, speed) {
		if (speed === undefined) {
			$(identitifer).fadeIn();
		} else {
			$(identitifer).fadeIn(speed);
		}
	}

	function fadeOut(identitifer, speed) {
		if (speed === undefined) {
			$(identitifer).fadeOut();
		} else {
			$(identitifer).fadeOut(speed);
		}
	}

	function isNotNavigationOrMetaKey(keyCode) {
		return keyCode !== 16 && // Shift
			   keyCode !== 17 && // Ctrl
			   keyCode !== 18 && // Alt
			   keyCode !== 27 && // ESC
			   !(keyCode >= 33 && keyCode <= 40) // Arrow keys and navigation
	}

	function showDialog() {
		fadeIn(".motdplug-overlay", "fast");

		setTimeout(() => {
			fadeIn(".motdplug-dialog", "fast");

			if (!isElementInViewport($(".motdplug-dialog"))) {
				$([document.documentElement, document.body]).animate({
					scrollTop: $(".motdplug-dialog").offset().top - 15
				}, 500);
			}
		}, 100);


	}

	function closeDialog() {
		fadeOut(".motdplug-dialog", "fast");

		setTimeout(() => fadeOut(".motdplug-overlay", "fast"), 100);
	}

	motdModule.config(function ($httpProvider) {
		$httpProvider.defaults.xsrfCookieName = "CSRF-TOKEN";
	});



//    █████████                       █████                       ████
//   ███░░░░░███                     ░░███                       ░░███
//  ███     ░░░   ██████  ████████   ███████   ████████   ██████  ░███   ██████  ████████
// ░███          ███░░███░░███░░███ ░░░███░   ░░███░░███ ███░░███ ░███  ███░░███░░███░░███
// ░███         ░███ ░███ ░███ ░███   ░███     ░███ ░░░ ░███ ░███ ░███ ░███████  ░███ ░░░
// ░░███     ███░███ ░███ ░███ ░███   ░███ ███ ░███     ░███ ░███ ░███ ░███░░░   ░███
//  ░░█████████ ░░██████  ████ █████  ░░█████  █████    ░░██████  █████░░██████  █████
	motdModule.controller('motdplugCtrl', function (motdPluginService, $http) {
		let me = this;
		me.service = motdPluginService;

		function createEditor(textArea, outputFieldSelector) {
			let codeMirrorEditor = CodeMirror.fromTextArea(textArea, {
				lineNumbers: true,
				mode       : "htmlmixed",
				extraKeys  : {"Ctrl-Space": "autocomplete"},
			});

			codeMirrorEditor.on("keyup", (cm, event) => {
					// Add auto completion on key up
					let keyCode = event.keyCode;

					if (isNotNavigationOrMetaKey(keyCode)) {
						fadeOut(".motdplug-checkmark");

						if (!cm.state.completionActive && // Enables keyboard navigation in autocomplete list
							keyCode !== 8 && // Backspace
							keyCode !== 13 && // Enter - do not open autocomplete list just after item has been selected in it
							keyCode !== 46) // DEL
						{
							CodeMirror.commands.autocomplete(cm, null, {completeSingle: false});
						}


						// Populate the output field
						let sanitized = HtmlSanitizer.sanitizeHtml(codeMirrorEditor.getValue());
						$(outputFieldSelector).html(sanitized);
					}
				}
			)

			return codeMirrorEditor;
		};
		// Define fields available to the Angular Controller
		me.htmlOutput = "JS Start typing in the editor on the left to see the changes here in real time.";
		me.htmlName   = "";


		me.dialogText = "placeholder";
		me.dialogOkHandler = () => {};

		me.displayMessageSaved = true;

		me.displayMessageAddError = false;
		me.messageAddErrorText    = "";

		me.selectActiveMessageWaiting = "Waiting on data...";
		me.selectActiveMessage = me.selectActiveMessageWaiting;
		me.messageNames = [me.selectActiveMessageWaiting];
		me.activeMessage = {name: "No active message"};

		me.newActiveCheckbox = false;

		// Init the code editor
		let htmlBodyName  = "motdplug-htmlBody";
		let htmlFooterName = "motdplug-htmlFooter";

		let htmlBodyOutName = ".motdplug-htmlBodyOutput";
		let htmlFooterOutName = ".motdplug-htmlFooterOutput";

		let motdplugHtmlBodyInput = document.getElementById(htmlBodyName);
		let motdplugFooterInput   = document.getElementById(htmlFooterName);


		let bodyEditor   = createEditor(motdplugHtmlBodyInput, ".motdplug-htmlBodyOutput");
		let footerEditor = createEditor(motdplugFooterInput, ".motdplug-htmlFooterOutput");

		me.saveMessageDialog = function () {
			me.dialogText = `Do you want to save the message '${me.htmlName.trim()}'?`;
			me.dialogOkHandler = () => {
				me.saveMessage();
				closeDialog();
			};

			showDialog();
		}

		me.deleteMessageDialog = function () {
			me.dialogText = `Do you really want to delete the '${me.selectActiveMessage}' message?`;
			me.dialogOkHandler = () => {
				me.deleteMessage();
				closeDialog();
			};

			showDialog();
		}

		me.deleteMessage = function() {
			motdPluginService.deleteMessage(me.selectActiveMessage, me);
		}

		me.saveMessage = function () {
			fadeOut(".formError");
			fadeOut(".motdplug-checkmark");

			let message = new Message(me.htmlName.trim(), me.getInputBody(), me.getInputFooter());

			if (me.newActiveCheckbox) {
				message.active = true;
				me.activeMessage = message;
			}

			motdPluginService.saveMessage(message, me);
		}

		me.getInputBody = function () {
			return bodyEditor.getValue();
		}

		me.setInputBody = function (val) {
			bodyEditor.setValue(val);

			let sanitized = HtmlSanitizer.sanitizeHtml(val);
			$(htmlBodyOutName).html(sanitized);
		}

		me.getInputFooter = function () {
			return footerEditor.getValue();
		}

		me.setInputFooter = function (val) {
			footerEditor.setValue(val);

			let sanitized = HtmlSanitizer.sanitizeHtml(val);
			$(htmlFooterOutName).html(sanitized);
		}

		me.getMessageNames = function () {
			motdPluginService.getMessageNames(me);
		}


		me.closeDialog = closeDialog;

		// Refresh data
		me.refreshData = function () {
			fadeOut(".motdplug-checkmark")
			me.newActiveCheckbox = false;

			motdPluginService.getMessageNames(me);

			if (me.selectActiveMessage != null)
				motdPluginService.getMessage(me.selectActiveMessage, me);
		}




		// Init data
		motdPluginService.getMessageNames(me);
		motdPluginService.getActiveMessage(me);
	});



//   █████████                                  ███
//  ███░░░░░███                                ░░░
// ░███    ░░░   ██████  ████████  █████ █████ ████   ██████   ██████
// ░░█████████  ███░░███░░███░░███░░███ ░░███ ░░███  ███░░███ ███░░███
//  ░░░░░░░░███░███████  ░███ ░░░  ░███  ░███  ░███ ░███ ░░░ ░███████
//  ███    ░███░███░░░   ░███      ░░███ ███   ░███ ░███  ███░███░░░
// ░░█████████ ░░██████  █████      ░░█████    █████░░██████ ░░██████  that handles http requests.
	motdModule.service('motdPluginService', function ($http) {
		return {
			deleteMessage: function (name, controller) {
				let url = PluginHelper.getPluginRestUrl(`motd/reference-plugin-service/deleteMessage/${name}`);

				$http.delete(url)
					 .then(
						 function onSuccess(response) {
							 controller.getMessageNames(controller);

							 controller.selectActiveMessage = controller.messageNames[0];

							 controller.service.getMessage(controller.selectActiveMessage, controller);

						 },

						 function onError(response) {
							 let {data} = response;

							 fadeIn(".formError");
							 controller.messageAddErrorText = data;

						 }
					 );
			},

			saveMessage: function (message, controller) {
				let url = PluginHelper.getPluginRestUrl("motd/reference-plugin-service/addMessage");

				$http.put(url, message, null)
					 .then(
						 function onSuccess(response) {
							 fadeIn(".motdplug-checkmark");

							 controller.getMessageNames(controller);
							 controller.selectActiveMessage = message.name;
							 controller.newActiveCheckbox = false;
						 },

						 function onError(response) {
							 let {data} = response;

							 fadeIn(".formError");
							 controller.messageAddErrorText = data;
						 }
					 );
			},

			getMessageNames: function (controller) {
				let url = PluginHelper.getPluginRestUrl("motd/reference-plugin-service/getMessageNames");

				$http.get(url)
					 .then(
						 function onSuccess(response) {
						 	controller.messageNames = JSON.parse(response.data);
						 },

						 function onError(response) {
							 let {data} = response;

							 fadeIn(".formError");
							 controller.messageAddErrorText = data;
						 }
					 );
			},

			getActiveMessage: function (controller) {
				let url = PluginHelper.getPluginRestUrl("motd/reference-plugin-service/getActiveMessage");

				$http.get(url)
					 .then(
						 function onSuccess(response) {
						 	let {name, body, footer} = JSON.parse(response.data);

							controller.activeMessage = new Message(name, body, footer);

							controller.selectActiveMessage = name;

							controller.htmlName = name;
							controller.setInputBody(body);
							controller.setInputFooter(footer);

						 },

						 function onError(response) {
							 let {data} = response;

							 fadeIn(".formError");
							 controller.messageAddErrorText = data;
						 }
					 );
			},

			getMessage: function (name, controller, successCallback) {
				let url = PluginHelper.getPluginRestUrl(`motd/reference-plugin-service/getMessage/${name}`);

				$http.get(url)
					 .then(
						 function onSuccess(response) {
						 	let {name, body, footer} = JSON.parse(response.data);

							controller.htmlName = name;
							controller.setInputBody(body);
							controller.setInputFooter(footer);


						 	if (successCallback !== undefined && successCallback !== null)
						 		successCallback();
						 },

						 function onError(response) {
							 let {data} = response;

							 fadeIn(".formError");
							 controller.messageAddErrorText = data;
						 }
					 );
			}

		};
	});
}