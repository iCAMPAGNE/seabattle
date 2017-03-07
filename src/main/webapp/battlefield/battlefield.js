'use strict';

var app = angular.module('myApp.battlefield', ['ngRoute']);

app.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/battlefield', {
    templateUrl: 'battlefield/battlefield.html',
    controller: 'BattlefieldCtrl'
  });
}])

.controller('BattlefieldCtrl', ['$scope', function($scope) {
	var arrayHorizontal = new Array(8);
	for (var i = 0; i < arrayHorizontal.length; i++) {
		arrayHorizontal[i] = i;
	}
	$scope.seaArray = arrayHorizontal;
	
	$scope.plons = 'plons';
	
	$scope.showStartButton = false;
	$scope.showEnemySea = false;
	
	$scope.handleDrop = function(item, bin) {
		    console.log('Item ' + item + ' has been dropped into ' + bin);
		  }
	
	$scope.start = function() {
		$scope.showEnemySea = true;
		$scope.showStartButton = false;
	}
	
	$scope.shoot = function(x,y) {
		alert(x + ',' + y);
	}
	
	$scope.dropped = function(a,b) {
        var undraggedElements = document.getElementsByClassName("undragged");
        if (undraggedElements.length === 0) {
          	$scope.showStartButton = true;
          }
	}

}]);

app.directive('draggable', function() {
  return function(scope, element) {
    // this gives us the native JS object
    var el = element[0];
    
    el.draggable = true;
    
    el.addEventListener(
      'dragstart',
      function(e) {
        e.dataTransfer.effectAllowed = 'move';
        e.dataTransfer.setData('Text', this.id);
        this.classList.add('drag');
        return false;
      },
      false
    );
    
    el.addEventListener(
      'dragend',
      function(e) {
        this.classList.remove('drag');
        return false;
      },
      false
    );
  }
});

app.directive('droppable', function() {
  return {
    scope: {
      drop: '&',
      bin: '='
    },
    link: function(scope, element) {
      // again we need the native object
      var el = element[0];
      
      el.addEventListener(
        'dragover',
        function(e) {
          e.dataTransfer.dropEffect = 'move';
          // allows us to drop
          if (e.preventDefault) e.preventDefault();
          this.classList.add('over');
          return false;
        },
        false
      );
      
      el.addEventListener(
        'dragenter',
        function(e) {
          this.classList.add('over');
          return false;
        },
        false
      );
      
      el.addEventListener(
        'dragleave',
        function(e) {
          this.classList.remove('over');
          return false;
        },
        false
      );
      
      el.addEventListener(
        'drop',
        function(e) {
          // Stops some browsers from redirecting.
          if (e.stopPropagation) e.stopPropagation();
          
          this.classList.remove('over');
          
          var binId = this.id;
          var item = document.getElementById(e.dataTransfer.getData('Text'));
          item.classList.remove('undragged');
          this.appendChild(item);

          
          // call the passed drop function
          scope.$apply(function(scope) {
            var fn = scope.drop();
            if ('undefined' !== typeof fn) {            
              fn(item.id, binId);
            }
          });
          
          return false;
        },
        false
      );
    }
  }
});