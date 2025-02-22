/**
* {{title}} Component
**/

((Drupal, once) => {
  Drupal.behaviors.{{titleCamelCase}} = {
    attach (context/*, settings*/) {
      // Write some JS magic here
      once('once-{{titleKebabCase}}', '.{{titleKebabCase}}', context)
        .forEach((componentItem) => {
        });
    },
    detach (/* context, settings */) {},
  };
})(Drupal, once);