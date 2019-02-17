// Copyright (c) 2017, the Dart project authors.  Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package de.jensklingenberg.kt_dart_builder.main.mixins

import de.jensklingenberg.kt_dart_builder.main.specs.Expression

/// A type of AST node that can have metadata [annotations].
interface HasAnnotations {
    /// Annotations as metadata on the node.
    val annotations: List<Expression>
}

/// Compliment to the [HasAnnotations] mixin for metadata [annotations].
interface HasAnnotationsBuilder {
    /// Annotations as metadata on the node.
    val annotations: MutableList<Expression>
}
