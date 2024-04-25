/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

(() => {
	'use strict';

	const motdWidgetFunction = () => {
		angular.module('sailpoint.home.desktop.app')
			   .controller('motdWidgetCtrl', ['$scope', 'motdWidgetService',
											  function ($scope, motdWidgetService) {
												  $scope.title = "";

												  motdWidgetService.getTitle($scope);

												  motdWidgetService.getActiveMessage();

											  }])
			   .service('motdWidgetService', ['$http', function ($http) {
				   return {
					   getTitle        : function ($scope) {
						   let url = PluginHelper.getPluginRestUrl("motd/reference-plugin-service/getTitle");

						   $http.get(url)
								.then(
									function onSuccess(response) {
										let {title} = JSON.parse(response.data);

										$scope.title = title;

										$("span[title='__motd__plugin_title__']").text(title);
										$('div.card-title:contains("__motd__plugin_title__")').text(title)
									},

									function onError(response) {
										let {data} = response;

										$(".motdplug-widget-body").html(HtmlSanitizer.sanitizeHtml(data));
									}
								);
					   },
					   getActiveMessage: function () {
						   let url = PluginHelper.getPluginRestUrl("motd/reference-plugin-service/getActiveMessage");

						   $http.get(url)
								.then(
									function onSuccess(response) {
										let {body, footer} = JSON.parse(response.data);

										$(".motdplug-widget-body").html(HtmlSanitizer.sanitizeHtml(body));
										$(".motdplug-widget-footer").html(HtmlSanitizer.sanitizeHtml(footer));
									},

									function onError(response) {
										let {data} = response;

										$(".motdplug-widget-body").html(HtmlSanitizer.sanitizeHtml(data));
									}
								);
					   }
				   }
			   }])
			   .directive('spMotdWidget', () => {
				   return {
					   restrict        : 'E',
					   scope           : {
						   widget: '=spWidget'
					   },
					   controller      : 'motdWidgetCtrl',
					   controllerAs    : 'motdWidgetCtrl',
					   bindToController: true,
					   templateUrl     : PluginHelper.getPluginFileUrl('motd', 'ui/html/widget/MOTD-Widget.html')
				   };
			   });
	}

	PluginHelper.addWidgetFunction(motdWidgetFunction);


	const replaceTitleInAddWidget = () => {
		const querySelector = document.querySelector('[ng-controller="DesktopHomeCtrl as homeCtrl"]');

		const overrideAddWidget = () => {
			const scope = angular.element(querySelector).scope();

			// Recursively check if the scope is loaded
			if (scope === undefined) {
				setTimeout(overrideAddWidget, 1000);
			} else {
				// Save the original addWidget function
				const superAddWidget = scope.homeCtrl.addWidget;

				scope.homeCtrl.addWidget = () => {
					// Call the super function
					superAddWidget();

					// Wait for the modal to load
					angular.element(document.getElementById("modal-content")).ready(() => {
						const scope = angular.element(document.getElementById("modal-content")).scope();

						// Save the original getLabel function
						const superGetLabel = scope.dialogCtrl.getLabel;

						scope.dialogCtrl.getLabel = (a) => {
							// Call the super function
							const result = superGetLabel(a);

							return result === "__motd__plugin_title__" ? "Message of The Day" : result;
						}
					});
				}
			}
		};

		if (querySelector) {
			overrideAddWidget();
		}
	}

	angular.element(document.querySelector('[ng-controller="DesktopHomeCtrl as homeCtrl"]')).ready(() => {
		replaceTitleInAddWidget();
	});
})();

