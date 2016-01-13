import Ember from 'ember';
import config from './config/environment';

const Router = Ember.Router.extend({
  location: config.locationType
});

Router.map(function() {

  this.route('proposals', {path: '/'});
  this.route('proposal', {path: '/proposal/:proposal_id'}, function() {
    this.route('new');
    this.route('cover-sheet');
    this.route('project-description');
    this.route('data-management-plan', { path: '/dmp' });
    this.route('bio-sketch');
    // this.route('bio-sketch', {resetNamespace: true});
  });


  //Page Not Found
  this.route('page-not-found', { path: '/*wildcard' });

});

export default Router;
