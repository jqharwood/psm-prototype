import Ember from 'ember';

export default Ember.Route.extend({

  model: function () {
    let proposal = this.modelFor('proposal');
    let dmp = proposal.get('dmp');
    return dmp;
  }

});
