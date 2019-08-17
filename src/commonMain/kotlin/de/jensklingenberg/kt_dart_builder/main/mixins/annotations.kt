// Copyright (c) 2017, the Dart project authors.  Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package de.jensklingenberg.kt_dart_builder.main.mixins

import de.jensklingenberg.kt_dart_builder.main.specs.Expression
import de.jensklingenberg.kt_dart_builder.main.specs.Reference

typealias bool = Boolean
typealias BuiltList<T> = List<T>
typealias ListBuilder<T> = ArrayList<T>
typealias BuiltMap<T,P> = Map<T,P>
typealias num = Int
typealias Allocate= (Reference) -> String


/**
 *A type of AST node that can have metadata [annotations].
*/
interface HasAnnotations {
    /// Annotations as metadata on the node.
    val annotations: BuiltList<Expression>
}

/// Compliment to the [HasAnnotations] mixin for metadata [annotations].
interface HasAnnotationsBuilder {
    /// Annotations as metadata on the node.
    val annotations: ListBuilder<Expression>
}
