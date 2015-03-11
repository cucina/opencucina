var app = angular.module('messageModule', [ 'ngRoute', 'restangular' ]);
app.config(function($routeProvider, RestangularProvider) {
    RestangularProvider.setBaseUrl('http://localhost:8080/');
    $routeProvider.when('/messagecreate', {
	controller : 'messageController',
	templateUrl : 'views/messagecreate.html'
    }).otherwise({
	redirectTo : '/'
    });

});

app.controller('messageController', [ '$scope', 'Restangular',
	function($scope, Restangular) {
	    $scope.listmessages = function() {
		Restangular.all('message/all').getList().then(function(messages) {
			$scope.messages = messages;
		});
	    };
	    $scope.createmessage = function() {
		var message = new Object();
		message.application = $scope.application;
		message.code = $scope.code;
		message.locale = $scope.locale;
		message.text = $scope.text;
		Restangular.all('message').post(message);
	    }
	} ]);
