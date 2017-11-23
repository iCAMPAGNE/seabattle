'use strict';

var app = angular.module('myApp.battlefield', ['ngRoute']);

app.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/battlefield', {
    templateUrl: 'battlefield/battlefield.html?dev=' + Math.floor(Math.random() * 100),
    controller: 'BattlefieldCtrl'
  });
}]);
app.service('SeabattleService', ['$http', '$rootScope', function ($http, $rootScope) {

    this.inGame = function (mySea, callback) {
        $http({
            method: 'POST',
            url: '/Seabattle/rest/inGame/' + $rootScope.userId,
            data: mySea
        })
        .then(function (response) {
            callback(response);
        }, function (response) {
            var result = {error:"Error connecting to host", dataLoading:"false"};
                callback(result);
        });
    };

    this.shoot = function (seaX, seaY, callback) {
        $http({
            method: 'GET',
            url: '/Seabattle/rest/shoot/' + $rootScope.userId + '?seaX=' + seaX + '&seaY=' + seaY
        })
        .then(function (response) {
                    callback(response);
        }, function (response) {
            var result = {error:"Error connecting to host", dataLoading:"false"};
            callback(result);
        });
    };

}]);

app.controller('BattlefieldCtrl', ['$rootScope', '$scope', '$compile', 'SeabattleService', function($rootScope, $scope, $compile, SeabattleService) {
	console.log('Controller started');
    $rootScope.userId = Math.floor(100000 * Math.random());
    console.log(' User ' + $rootScope.userId + ' starts playing');
    $scope.status = 'PLACE_SHIPS';
	
	
	$scope.seaArray = new Array(4);
	for (var v = 0; v < $scope.seaArray.length; v++) {
		$scope.seaArray[v] = new Array(4);
		for (var h = 0; h < $scope.seaArray[v].length; h++) {
			$scope.seaArray[v][h] = { status: 0, x:v, y:h};
		}
	}
	
	$scope.mySeaArray = new Array(4);
	for (var v = 0; v < $scope.mySeaArray.length; v++) {
		$scope.mySeaArray[v] = new Array(4);
		for (var h = 0; h < $scope.mySeaArray[v].length; h++) {
			$scope.mySeaArray[v][h] = { status: 0, x:v, y:h};
		}
	}

    var ws = new WebSocket("ws://localhost:8080/Seabattle/socket");
    ws.onopen = function(){  
        console.log("Socket has been opened!");  
        
        ws.send($rootScope.userId); // Send id to backend for registering.
    };
    
    ws.onmessage = function(message) {
    	console.log("Socket message received: " + message.data);
    	var content = JSON.parse(message.data);
    	if (content.command == 'shoot') {
        	var shot = content.shot;
          	$scope.mySeaArray[shot.x][shot.y].status = shot.status;
           	console.log("schot op " + shot.x + ',' + shot.y);
    	}
    	if (content.command == 'enemyStatus') {
    		$scope.enemyStatus = content.state;
    	}
    	if (content.command == 'status') {
    		$scope.status = content.state;
    	}
       	$scope.$apply();
    };
	
	$scope.handleDrop = function(item, bin) {
		    console.log('Item ' + item + ' has been dropped into ' + bin);
		  }
	
	$scope.start = function() {
		var item1_xy = document.querySelector('#item1').parentNode.id.substring(5).split(".");
		$scope.mySeaArray[parseInt(item1_xy[1])][parseInt(item1_xy[0])].status = 2;
		document.querySelector('#item1').remove();
		var item2_xy = document.querySelector('#item2').parentNode.id.substring(5).split(".");
		$scope.mySeaArray[parseInt(item2_xy[1])][parseInt(item2_xy[0])].status = 2;
		document.querySelector('#item2').remove();
		var item3_xy = document.querySelector('#item3').parentNode.id.substring(5).split(".");
		$scope.mySeaArray[parseInt(item3_xy[1])][parseInt(item3_xy[0])].status = 2;
		document.querySelector('#item3').remove();
		
		var mySeaJson = '[';
		for (var v = 0; v < $scope.mySeaArray.length; v++) {
			for (var h = 0; h < $scope.mySeaArray[v].length; h++) {
				var wave = '{"h":"' + h + '","v":"' + v + '","status":"' + $scope.mySeaArray[h][v].status + '"}';
				if (h !== 0 || v !== 0) {
					mySeaJson += ',';
				}
				mySeaJson += wave;
			}
		}
		mySeaJson += ']';
		console.log(mySeaJson);
		SeabattleService.inGame(mySeaJson, function(response) {
			console.log(response);
			
			var cells = document.querySelectorAll('.seacell');
			console.log(`  !!!  cells: ${cells.length}`);
			[].forEach.call(cells, function(cell) {
				  cell.removeAttribute('draggable');
				});
		});
	    $scope.status = 'IN_GAME';
	}
	
	$scope.shoot = function(x,y) {
		if ($scope.status == 'SHOOTING') {
			SeabattleService.shoot(x,y, function (response) {
		    	$scope.seaArray[x][y].status = parseInt(response.data.status);
				console.log(response.data.status + ', response: ' + JSON.stringify(response));
			});
		}
	}
	
	$scope.dropped = function(x,y) {
		//$scope.mySeaArray[x][y].status = 2;
        var undraggedElements = document.getElementsByClassName("undragged");
        if (undraggedElements.length === 0) {
            $scope.status = 'REPLACE_SHIPS';
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
